package com.Edstrom;

import com.Edstrom.entity.Member;
import com.Edstrom.entity.Rental;
import com.Edstrom.exception.InvalidEmailException;
import com.Edstrom.exception.InvalidMemberDataException;
import com.Edstrom.repository.MemberRepository;
import com.Edstrom.repository.MemberRepositoryImpl;
import com.Edstrom.repository.RentalRepository;
import com.Edstrom.repository.RentalRepositoryImpl;
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

public class Main extends Application {

    private SessionFactory sessionFactory;
    private MemberRepository memberRepository;
    private RentalRepository rentalRepository;
    private MembershipService membershipService;
    private RentalService rentalService;
    private ObservableList<Member> members;

    TableView<Member> memberTable;

    TextField nameField, emailField;
    Label messageLabel;

    @Override
    public void init() {

        sessionFactory = HibernateUtil.getSessionFactory();

        //Repos
        memberRepository = new MemberRepositoryImpl(sessionFactory);
        rentalRepository = new RentalRepositoryImpl(sessionFactory);

        //Service
        membershipService = new MembershipService(memberRepository);
        rentalService = new RentalService(rentalRepository);



    }

    @Override
    public void start(Stage stage) {
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

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");


        VBox root = new VBox(10, memberTable, new HBox(10,
                nameField, emailField, addButton, deleteButton), messageLabel);
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
    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }



    public static void main (String[]args){
            launch(args);
        }
    }
