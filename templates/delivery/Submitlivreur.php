<?php
// Inclure le fichier de configuration de la base de données
require_once 'config.php';

// Vérifier si le formulaire a été soumis
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Récupérer les données du formulaire
    $nom = $_POST['nom'];
    $prenom = $_POST['prenom'];
    $region = $_POST['region'];
    $email = $_POST['email'];
    $telephone = $_POST['telephone'];

    // Préparer la requête SQL pour insérer un nouveau livreur
    $sql = "INSERT INTO livreurs (nom, prenom, region, email, telephone) VALUES (?, ?, ?, ?, ?)";

    // Préparer la déclaration SQL
    if ($stmt = $mysqli->prepare($sql)) {
        // Liaison des paramètres à la déclaration SQL
        $stmt->bind_param("sssss", $nom, $prenom, $region, $email, $telephone);

        // Exécution de la déclaration
        if ($stmt->execute()) {
            // Redirection vers une page de succès ou autre action après l'ajout du livreur
        //    header("location: success.php");
            exit();
        } else {
            echo "Erreur lors de l'exécution de la requête : " . $stmt->error;
        }

        // Fermer la déclaration
        $stmt->close();
    } else {
        echo "Erreur lors de la préparation de la requête : " . $mysqli->error;
    }

    // Fermer la connexion à la base de données
    $mysqli->close();
}
?>
