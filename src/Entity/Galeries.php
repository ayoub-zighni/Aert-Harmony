<?php

namespace App\Entity;

use App\Repository\GaleriesRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: GaleriesRepository::class)]
class Galeries
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private $id;

    #[ORM\Column(length:255)]
    #[Assert\NotBlank(message: "Name is required !")]
    private $nom = null;

    #[ORM\Column(length:255)]
    #[Assert\NotBlank(message: "Address is required !")]
    private $adresse = null;

    #[ORM\Column(length:255)]
    #[Assert\NotBlank(message: "City is required !")]
    private $ville = null;

    #[ORM\Column(length:255)]
    #[Assert\NotBlank(message: "Country is required !")]
    private $pays = null;

    #[ORM\Column]
    #[Assert\Positive(message: "Capacity must be greater then 0")]
    private $capaciteMax = NULL;

    #[ORM\Column(length:255)]
    private $image;

    public function getId(): ?string
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;

        return $this;
    }

    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(string $adresse): static
    {
        $this->adresse = $adresse;

        return $this;
    }

    public function getVille(): ?string
    {
        return $this->ville;
    }

    public function setVille(string $ville): static
    {
        $this->ville = $ville;

        return $this;
    }

    public function getPays(): ?string
    {
        return $this->pays;
    }

    public function setPays(string $pays): static
    {
        $this->pays = $pays;

        return $this;
    }

    public function getCapaciteMax(): ?string
    {
        return $this->capaciteMax;
    }

    public function setCapaciteMax(string $capaciteMax): static
    {
        $this->capaciteMax = $capaciteMax;

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(string $image): static
    {
        $this->image = $image;

        return $this;
    }

}