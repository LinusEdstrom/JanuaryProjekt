package com.Edstrom;

import com.Edstrom.entity.Member;
import com.Edstrom.repository.MemberRepository;
import com.Edstrom.repository.MemberRepositoryImpl;
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
    private ObservableList<Member> members;

    @Override
    public void init() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        memberRepository = new MemberRepositoryImpl(sessionFactory);
    }

    @Override
    public void start(Stage stage) {
        TableView<Member> table = new TableView<>();

        TableColumn<Member, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Member, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Member, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idCol, nameCol, emailCol);

        members = FXCollections.observableArrayList(memberRepository.findAll());
        table.setItems(members);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button addButton = new Button("Add Member");
        addButton.setOnAction(e -> {
            Member member = new Member(nameField.getText(), emailField.getText());
            memberRepository.save(member);
            members.setAll(memberRepository.findAll());
            nameField.clear();
            emailField.clear();
        });

        VBox root = new VBox(10, table, new HBox(10, nameField, emailField, addButton));
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Members");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
