package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {

    @FXML
    private WebView webView;

    private WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
    }

    @FXML
    public void showMap() {
        // Charger le contenu HTML avec la carte Google Maps
        webEngine.loadContent("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Google Maps Example</title>\n" +
                "    <style>\n" +
                "        /* Adjust the size and position of the map */\n" +
                "        #mapouter {\n" +
                "            position: relative;\n" +
                "            text-align: right;\n" +
                "            height: 500px; /* Adjust the height as needed */\n" +
                "            width: 500px; /* Adjust the width as needed */\n" +
                "        }\n" +
                "\n" +
                "        #gmap_canvas2 {\n" +
                "            overflow: hidden;\n" +
                "            background: none !important;\n" +
                "            height: 500px; /* Adjust the height as needed */\n" +
                "            width: 500px; /* Adjust the width as needed */\n" +
                "        }\n" +
                "\n" +
                "        #gmap_canvas {\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "            border: 0;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"mapouter\">\n" +
                "    <div id=\"gmap_canvas2\">\n" +
                "        <iframe id=\"gmap_canvas\"\n" +
                "                src=\"https://maps.google.com/maps?q=Esprit%20Ghazela&t=k&z=16&output=embed\"\n" +
                "                frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
