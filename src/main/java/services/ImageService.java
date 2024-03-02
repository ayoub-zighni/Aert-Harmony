package services;

import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageService {

    Connection connection;
    public ImageService(){ connection = MyDatabase.getInstance().getConnection();}
    public void saveImage(String imagePath) throws SQLException {
        String sql = "INSERT INTO images (img) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, imagePath);
            statement.executeUpdate();
        }
    }

    public List<String> getAllImagePaths() throws SQLException {
        List<String> imagePaths = new ArrayList<>();
        String sql = "SELECT img FROM images";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String imagePath = resultSet.getString("img");
                imagePaths.add(imagePath);
            }
        }
        return imagePaths;
    }
    }