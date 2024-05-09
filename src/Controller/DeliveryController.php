<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use App\Entity\Commande;
use Doctrine\Persistence\ManagerRegistry;
class DeliveryController extends AbstractController
{
    #[Route('/delivery', name: 'app_delivery')]
    public function index(): Response
    {
        return $this->render('delivery/delivery.html.twig', [
            'controller_name' => 'DeliveryController',
        ]);
    }
}
