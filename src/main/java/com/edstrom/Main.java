package com.edstrom;

import com.edstrom.dto.RentableItemDTO;
import com.edstrom.entity.*;
import com.edstrom.exception.InvalidEmailException;
import com.edstrom.exception.InvalidMemberDataException;
import com.edstrom.exception.RentalErrorException;
import com.edstrom.repository.*;
import com.edstrom.service.MembershipService;
import com.edstrom.service.RentalService;
import com.edstrom.util.HibernateUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private final ObservableList<RentableItemDTO> availableItems =
            FXCollections.observableArrayList();

    private final ObservableList<Rental> activeRentalsList = FXCollections.observableArrayList();


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

    TableView<Rental> rentalHistoryView = new TableView<>();

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
        rentalService = new RentalService(rentalRepository);
        membershipService = new MembershipService(memberRepository);
    }

    @Override
    public void start(Stage stage) {

        activeRentalsView = new ListView<>();
        activeRentalsView.setItems(activeRentalsList);
        populateActiveRentals();

        objectsListView = new ListView<>();
        objectsListView.setItems(availableItems);
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

        rentalHistoryView = new TableView<>();
        rentalHistoryView.setVisible(false);
        rentalHistoryView.setManaged(false);

        TableColumn<Rental, String> itemColumn = new TableColumn<>("Items");
        itemColumn.setMinWidth(450);
        itemColumn.setMaxWidth(Double.MAX_VALUE);
        TableColumn<Rental, BigDecimal> totalPriceColumn = new TableColumn<>("Total Price");
        TableColumn<Rental, LocalDate> rentalDateColumn = new TableColumn<>("Rental Date");
        TableColumn<Rental, LocalDate> returnDateColumn = new TableColumn<>("Return Date");

        rentalHistoryView.getColumns().addAll(itemColumn, totalPriceColumn, rentalDateColumn, returnDateColumn);


        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemNames"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        rentalDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));


        Button addButton = new Button("Add Member");
        addButton.setOnAction(e -> addButtonClicked());

        Button deleteButton = new Button("Delete Member");
        deleteButton.setOnAction(e -> deleteButtonClicked());

        Button rentButton = new Button("Rent selected Objects");
        rentButton.setOnAction(e -> rentButtonClicked());

        Button returnButton = new Button("Return this rental and pay");
        returnButton.setOnAction(e -> returnButtonClicked());

        Button historyButton = new Button("See members history");
        historyButton.setOnAction(e -> historyButtonClicked());

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: red;");
        exitButton.setOnAction(e -> exitButtonClicked());

        Button exitHistoryButton = new Button("Exit");
        exitHistoryButton.setStyle("-fx-background-color: red;");
        exitHistoryButton.setOnAction(e->exitHistoryButtonClicked());

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");


        VBox root = new VBox(10, memberTable,
        objectsListView, activeRentalsView,
                new HBox(10, nameField, emailField,
                        addButton, deleteButton, rentButton, returnButton,
                        historyButton, exitButton), messageLabel, rentalHistoryView);
        stage.setScene(new Scene(root, 1400, 1000));
        stage.setTitle(" WIGELLS MOVIE GAME & COSTUME RENTALS");
        stage.show();
    }

    private void historyButtonClicked() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showError("Select a member for history view");
            return;
        }
        try {
            List<Rental> history = rentalService.findRentalsByMember(selectedMember);
            rentalHistoryView.getItems().setAll(history);

            rentalHistoryView.setVisible(true);
            rentalHistoryView.setManaged(true);
        }catch (RentalErrorException e) {
            showError("Error observing history " + e.getMessage());
            rentalHistoryView.getItems().clear();
            rentalHistoryView.setVisible(false);
            rentalHistoryView.setManaged(false);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error loading rental history");
            rentalHistoryView.getItems().clear();
            rentalHistoryView.setVisible(false);
            rentalHistoryView.setManaged(false);
        }
    }

    public void addButtonClicked() {
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
        } catch (InvalidEmailException e) {
            showError(" Invalid email");
        }
    }

    public void deleteButtonClicked() {
        try {
            Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

            membershipService.deleteMember(selectedMember);
            members.remove(selectedMember);

            showSuccess("Member " + selectedMember.getName() + " deleted");

        } catch (InvalidMemberDataException e) {
            showError("Select a member to delete");
        }
    }

    private void exitButtonClicked() {
        Platform.exit();
    }

    private void exitHistoryButtonClicked() {
        activeRentalsView.setVisible(false);
        activeRentalsView.setManaged(false);
        activeRentalsView.getItems().clear();
    }

    private void returnButtonClicked() {
        Rental selectedRental = activeRentalsView.getSelectionModel().getSelectedItem();

        if (selectedRental == null) {
            showError("Choose a rental to return");
            return;
        }

        try {
            BigDecimal totalPrice = rentalService.returnRental(selectedRental);

            showSuccess(
                    "Rental returned for " + selectedRental.getMember().getName() +
                            ". Total to pay: " + totalPrice.setScale(2, RoundingMode.HALF_UP)
            );

            Platform.runLater(() -> {
                populateActiveRentals();
                populateAvailableItems();
                populateMemberHistory(selectedRental.getMember());
            });

        } catch (RentalErrorException e) {
            showError(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error while returning rental");
        }
    }

    private void rentButtonClicked() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
        List<RentableItemDTO> selectedItems = objectsListView.getSelectionModel().getSelectedItems();

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
                    ro.setPriceCharged(item.getBasePrice());
                    ro.setDisplayName(item.getDisplayName());
                    ro.setRentalType(item.getRentalType());
                    return ro;
                })
                .collect(Collectors.toList());

        try {
            rentalService.createRental(selectedMember, rentedObjects);


            Platform.runLater(() -> {
                populateActiveRentals();
                populateAvailableItems();
                objectsListView.getSelectionModel().clearSelection();
            });

            showSuccess("Rental created for " + selectedMember.getName());

        }catch(RentalErrorException e) {
            showError("Error could not create rental" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected creating rental error");
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
        availableItems.setAll(rentalService.findAvailableItems());
    }

    private void populateActiveRentals() {
        List<Rental> rentals = rentalService.findAllActiveRentals();
        Platform.runLater(() -> {
            activeRentalsList.setAll(rentals);
            activeRentalsView.refresh();
        });
    }

    private void populateAvailableItems() {
        availableItems.setAll(rentalService.findAvailableItems());
    }
    private void populateMemberHistory(Member member) {
        if(member == null || member.getId() == null) {
            rentalHistoryView.getItems().clear();
            return;
        }
        try {
            List<Rental> history = rentalService.findRentalsByMember(member);
            rentalHistoryView.getItems().setAll(history);
        }catch (RentalErrorException e) {
            showError("Error loading members history" + e.getMessage());
            rentalHistoryView.getItems().clear();
        }catch (Exception e) {
        e.printStackTrace();
        showError("Unexpected error while loading history");
        rentalHistoryView.getItems().clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}