<?php

namespace App\Form;

use App\Entity\Facture;
use App\Entity\Panier;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\FormError;

class FactureType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('client')
            ->add('caissier')
            ->add('panier', EntityType::class, [
                'class' => Panier::class,
                'choice_label' => function (Panier $panier) {
                    // Customize the label to include both the nomProduit and montant (prix)
                    return sprintf('%s - Montant: %s', $panier->getNomProduit(), $panier->getPrix());
                },
            ])
            ->add('percu', NumberType::class, [
                'scale' => 2, // Ensure correct decimal scale
            ])
            ->addEventListener(FormEvents::SUBMIT, function (FormEvent $event) {
                $form = $event->getForm();
                $facture = $event->getData();
                $panier = $facture->getPanier();

                // Calculate the montant using the prix from Panier
                $montant = $panier->getPrix();
                $facture->setMontant($montant);

                $percu = $form->get('percu')->getData();

                if ($montant > $percu) {
                    $form->get('percu')->addError(new FormError('Le perçu doit être supérieur ou égal au montant.'));
                }
            });
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Facture::class,
        ]);
    }
}
