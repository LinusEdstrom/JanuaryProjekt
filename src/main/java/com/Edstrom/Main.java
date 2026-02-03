package com.Edstrom;

import com.Edstrom.entity.*;
import com.Edstrom.exception.InvalidEmailException;
import com.Edstrom.exception.InvalidMemberDataException;
import com.Edstrom.repository.*;
import com.Edstrom.service.MembershipService;
import com.Edstrom.service.RentalService;
import com.Edstrom.util.HibernateUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private SessionFactory sessionFactory;
    private MemberRepository memberRepository;
    private RentalRepository rentalRepository;
    private MembershipService membershipService;
    private RentalService rentalService;
    private MovieRepository movieRepository;
    private GameRepository gameRepository;
    private CostumeRepository costumeRepository;
    private ObservableList<Member> members;

    TableView<Member> memberTable;

    ListView<RentableItemDTO> objectsListView;

    ListView<Rental> activeRentalsView;

    TextField nameField, emailField;
    Label messageLabel;

    @Override
    public void init() {

        sessionFactory = HibernateUtil.getSessionFactory();

        //Repos
        memberRepository = new MemberRepositoryImpl(sessionFactory);
        rentalRepository = new RentalRepositoryImpl(sessionFactory);
        movieRepository = new MovieRepositoryImpl(sessionFactory);
        gameRepository = new GameRepositoryImpl(sessionFactory);
        costumeRepository = new CostumeRepositoryImpl(sessionFactory);

        //Service
        membershipService = new MembershipService(memberRepository);
        rentalService = new RentalService(rentalRepository);



    }

    @Override
    public void start(Stage stage) {

        activeRentalsView = new ListView<>();
        populateActiveRentals();

        objectsListView = new ListView<>();
        objectsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadRentableItems();

         memberTable = new TableView<>();

        TableColumn<Member, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Member, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Member, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        memberTable.getColumns().addAll(idCol, nameCol, emailCol);


        //TODO ska den här till memberRepository eller spelar det ingen roll längre?
        //TODO fix and trix bruh
        members = FXCollections.observableArrayList(membershipService.getAllMembers());
        memberTable.setItems(members);

        nameField = new TextField();
        nameField.setPromptText("Name");

        emailField = new TextField();
        emailField.setPromptText("Email");

        Button addButton = new Button("Add Member");
        addButton.setOnAction(e -> addButtonClicked());

        Button deleteButton = new Button("Delete Member");
        deleteButton.setOnAction(e -> deleteButtonClicked());

        Button rentButton = new Button("Rent selected Objects");
        rentButton.setOnAction(e -> rentButtonClicked());

        Button returnButton = new Button("Return this rental and pay");
        returnButton.setOnAction(e->returnButtonClicked());

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");


        VBox root = new VBox(10, memberTable, objectsListView, activeRentalsView,
                new HBox(10, nameField, emailField,
                        addButton, deleteButton, rentButton, returnButton), messageLabel);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Members");
        stage.show();
    }
        public void addButtonClicked () {
            try {
               Member newMember = membershipService.createMember(
                        nameField.getText(),
                        emailField.getText()
                );
                members.add(newMember);

                nameField.clear();
                emailField.clear();
                showSuccess("Member " + newMember.getName() + " email "
                        + newMember.getEmail() + " successfully created");
            } catch (InvalidMemberDataException e) {
                showError(" Invalid data input for a members name");
            }catch (InvalidEmailException e) {
                showError(" Invalid email");
            }
        }
        public void deleteButtonClicked (){
        try {
            Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

            membershipService.deleteMember(selectedMember);
            members.remove(selectedMember);

            showSuccess("Member " +selectedMember.getName() + " deleted");

        }catch(InvalidMemberDataException e) {
            showError("Select a member to delete");
        }
        }
        private void returnButtonClicked() {
            Rental selectedRental = activeRentalsView.getSelectionModel().getSelectedItem();

            if (selectedRental == null) {
                showError("Choose a rental to return");
                return;
            }
            try {
                BigDecimal totalPrice = rentalService.returnRental(selectedRental);

                populateActiveRentals();
                populateAvailableItems();

                showSuccess("Rental returned for " + selectedRental.getMember().getName() +
                        " Total to pay " + totalPrice.setScale(2, RoundingMode.HALF_UP));
            }catch(Exception e){
                e.printStackTrace();
                showError("Error could not return rental");
            }
        }
    private void rentButtonClicked() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        List<RentableItemDTO> selectedItems =
                objectsListView.getSelectionModel().getSelectedItems();

        if (selectedMember == null) {
            showError("Select a member for the rental");
            return;
        }
        if (selectedItems == null || selectedItems.isEmpty()) {
            showError("Select at least one object to rent");
            return;
        }
        List<RentedObject> rentedObjects = selectedItems.stream()
                .map(item -> {
                    RentedObject ro = new RentedObject();
                    ro.setItemId(item.getId());
                    ro.setRentalType(item.getRentalType());
                    ro.setPriceCharged(item.getBasePrice());
                    ro.setRentalDate(LocalDate.now());
                    return ro;
                })
                .collect(Collectors.toList());
        try {
            rentalService.createRental(selectedMember, rentedObjects);
            showSuccess("Rental created for " + selectedMember.getName());
            populateActiveRentals();
            populateAvailableItems();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error could not create rental");
        }
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }
    private void loadRentableItems() {
        List<RentableItemDTO> allItems = new ArrayList<>();

        // Movies
        movieRepository.findAll().forEach(movie ->
                allItems.add(new RentableItemDTO(
                        movie.getId(),
                        "[MOVIE] " + movie.getTitle() + " (" + movie.getGenre() + "," +
                                " " + movie.getLength() + " min)",
                        movie.getBasePrice(),
                        RentalType.MOVIE
                ))
        );
        // Games
        gameRepository.findAll().forEach(game ->
                allItems.add(new RentableItemDTO(
                        game.getId(),
                        "[GAME] " + game.getName() + " — " + game.getDescription(),
                        game.getBasePrice(),
                        RentalType.GAME
                ))
        );

        // Costumes
        costumeRepository.findAll().forEach(costume ->
                allItems.add(new RentableItemDTO(
                        costume.getId(),
                        "[COSTUME] " + costume.getDescription() + " (Size: " + costume.getSize() + ")",
                        costume.getBasePrice(),
                        RentalType.COSTUME
                ))
        );
        objectsListView.setItems(FXCollections.observableArrayList(allItems));
    }
    private void populateActiveRentals() {
        activeRentalsView.getItems().setAll(rentalService.findAllActiveRentals());
    }
    private void populateAvailableItems() {
        objectsListView.getItems().setAll(rentalService.findAvailableItems());
    }


    public static void main (String[]args){
            launch(args);
        }

}