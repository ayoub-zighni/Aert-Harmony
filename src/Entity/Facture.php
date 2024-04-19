<?php

namespace App\Entity;

use App\Repository\FactureRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: FactureRepository::class)]
class Facture
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id;

    #[ORM\Column(type: 'string', length: 255)]
    private ?string $client;

    #[ORM\Column(type: 'string', length: 255)]
    private ?string $caissier;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
    private ?float $montant;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
    private ?float $percu;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
    private ?float $rendu;


    #[ORM\ManyToOne(targetEntity: Panier::class)]
    #[ORM\JoinColumn(name: 'id_panier', referencedColumnName: 'id')]
    private ?Panier $panier;
    
    public function getId(): ?int
    {
        return $this->id;
    }

    public function getClient(): ?string
    {
        return $this->client;
    }

    public function setClient(string $client): self
    {
        $this->client = $client;

        return $this;
    }

    public function getCaissier(): ?string
    {
        return $this->caissier;
    }

    public function setCaissier(string $caissier): self
    {
        $this->caissier = $caissier;

        return $this;
    }

    public function getMontant(): ?float
    {
        return $this->montant;
    }

    public function setMontant(float $montant): self
    {
        $this->montant = $montant;

        return $this;
    }

    public function getPercu(): ?float
    {
        return $this->percu;
    }

    public function setPercu(float $percu): self
    {
        $this->percu = $percu;

        return $this;
    }

    public function getRendu(): ?float
    {
        return $this->rendu;
    }

    public function setRendu(float $rendu): self
    {
        $this->rendu = $rendu;

        return $this;
    }



    public function getPanier(): ?Panier
    {
        return $this->panier;
    }

    public function setPanier(?Panier $panier): self
    {
        $this->panier = $panier;

        return $this;
    }
}
