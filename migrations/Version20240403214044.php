<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240403214044 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE facture (id INT AUTO_INCREMENT NOT NULL, client VARCHAR(255) NOT NULL, caissier VARCHAR(255) NOT NULL, montant DOUBLE PRECISION NOT NULL, preçu DOUBLE PRECISION NOT NULL, rendu DOUBLE PRECISION NOT NULL, id_panier INT NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4');
        $this->addSql('ALTER TABLE panier DROP id_panier');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE facture');
        $this->addSql('ALTER TABLE panier ADD id_panier INT NOT NULL');
    }
}
