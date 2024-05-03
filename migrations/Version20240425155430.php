<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240425155430 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE commentaire (id INT AUTO_INCREMENT NOT NULL, produit_id INT NOT NULL, message VARCHAR(255) NOT NULL, created_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', INDEX IDX_67F068BCF347EFB (produit_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL, available_at DATETIME NOT NULL, delivered_at DATETIME DEFAULT NULL, INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT FK_67F068BCF347EFB FOREIGN KEY (produit_id) REFERENCES produits (id)');
        $this->addSql('ALTER TABLE categorie CHANGE idcat idcat BIGINT AUTO_INCREMENT NOT NULL, CHANGE nom nom VARCHAR(255) NOT NULL');
        $this->addSql('ALTER TABLE produits DROP FOREIGN KEY fk_idcat');
        $this->addSql('ALTER TABLE produits DROP FOREIGN KEY fk_idcat');
        $this->addSql('ALTER TABLE produits CHANGE idcategorie idcategorie BIGINT DEFAULT NULL, CHANGE nom nom VARCHAR(255) NOT NULL, CHANGE description description VARCHAR(255) NOT NULL, CHANGE image image VARCHAR(255) NOT NULL, CHANGE stock stock VARCHAR(255) NOT NULL, CHANGE qr_code qr_code VARCHAR(255) NOT NULL');
        $this->addSql('ALTER TABLE produits ADD CONSTRAINT FK_BE2DDF8C37667FC1 FOREIGN KEY (idcategorie) REFERENCES categorie (idcat)');
        $this->addSql('DROP INDEX fk_idcat ON produits');
        $this->addSql('CREATE INDEX IDX_BE2DDF8C37667FC1 ON produits (idcategorie)');
        $this->addSql('ALTER TABLE produits ADD CONSTRAINT fk_idcat FOREIGN KEY (idcategorie) REFERENCES categorie (idcat) ON UPDATE CASCADE ON DELETE CASCADE');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY FK_67F068BCF347EFB');
        $this->addSql('DROP TABLE commentaire');
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('ALTER TABLE categorie CHANGE idcat idcat INT AUTO_INCREMENT NOT NULL, CHANGE nom nom VARCHAR(50) NOT NULL');
        $this->addSql('ALTER TABLE produits DROP FOREIGN KEY FK_BE2DDF8C37667FC1');
        $this->addSql('ALTER TABLE produits DROP FOREIGN KEY FK_BE2DDF8C37667FC1');
        $this->addSql('ALTER TABLE produits CHANGE idcategorie idcategorie INT NOT NULL, CHANGE nom nom VARCHAR(50) NOT NULL, CHANGE description description VARCHAR(50) NOT NULL, CHANGE image image VARCHAR(50) NOT NULL, CHANGE stock stock VARCHAR(50) NOT NULL, CHANGE qr_code qr_code VARCHAR(255) DEFAULT NULL');
        $this->addSql('ALTER TABLE produits ADD CONSTRAINT fk_idcat FOREIGN KEY (idcategorie) REFERENCES categorie (idcat) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('DROP INDEX idx_be2ddf8c37667fc1 ON produits');
        $this->addSql('CREATE INDEX fk_idcat ON produits (idcategorie)');
        $this->addSql('ALTER TABLE produits ADD CONSTRAINT FK_BE2DDF8C37667FC1 FOREIGN KEY (idcategorie) REFERENCES categorie (idcat)');
    }
}
