<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240403214648 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE facture (id INT AUTO_INCREMENT NOT NULL, client VARCHAR(255) NOT NULL, caissier VARCHAR(255) NOT NULL, montant NUMERIC(10, 2) NOT NULL, percu NUMERIC(10, 2) NOT NULL, rendu NUMERIC(10, 2) NOT NULL, id_panier INT DEFAULT NULL, INDEX IDX_FE8664102FBB81F (id_panier), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4');
        $this->addSql('ALTER TABLE facture ADD CONSTRAINT FK_FE8664102FBB81F FOREIGN KEY (id_panier) REFERENCES panier (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE facture DROP FOREIGN KEY FK_FE8664102FBB81F');
        $this->addSql('DROP TABLE facture');
    }
}
