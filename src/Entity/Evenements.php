<?php

namespace App\Entity;

use DateTime;
use App\Repository\EvenementsRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: EvenementsRepository::class)]
class Evenements
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private $id;

    #[ORM\Column(length:255)]
    #[Assert\NotBlank(message: "Name is required !")]
    private $nom = null;

    #[ORM\Column(type: "datetime")]
    #[Assert\GreaterThan(
        "today",
        message:"The date cannot be before today"
    )]
    private ?DateTime $date = null;

    #[ORM\Column(type: "time", nullable: true)]
    #[Assert\NotNull(message: "The time field is required")]
    private ?DateTime $heure = null;

    #[ORM\Column(length:65535)]
    #[Assert\NotBlank(message: "Your Event must have a description !")]
    #[Assert\Length(min: 20, minMessage: "Description must contain at least 20 characters")]
    private $description = null;

    #[ORM\Column(length:255)]
    private $image;

    #[ORM\Column]
    #[ORM\ManyToOne(targetEntity: "Galeries")]
    #[ORM\JoinColumn(name: "GalerieID", referencedColumnName: "ID", onDelete: "CASCADE")]
    private ?int $galerieid;

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

    public function getDate(): ?\DateTime
    {
        return $this->date;
    }

    public function setDate(\DateTime $date): static
    {
        $this->date = $date;

        return $this;
    }

    public function getHeure(): ?DateTime
    {
        return $this->heure;
    }

    public function setHeure(?DateTime $heure): static
    {
        $this->heure = $heure;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;

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

    public function getGalerieid(): ?int
    {
        return $this->galerieid;
    }

    public function setGalerieid(int $galerieid): static
    {
        $this->galerieid = $galerieid;

        return $this;
    }


}
