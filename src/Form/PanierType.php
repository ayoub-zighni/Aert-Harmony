<?php

namespace App\Form;

use App\Entity\Panier;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class PanierType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomProduit')
            ->add('prix')
            ->add('etatStock', ChoiceType::class, [
                'choices' => [
                    'en Stock' => 'en Stock',
                    'sur commande' => 'sur commande',
                ],
                'placeholder' => 'Choisissez un état',
                'required' => true, // Set to false if you want an empty value to be allowed
            ])
            ->add('quantite')
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Panier::class,
        ]);
    }
}
