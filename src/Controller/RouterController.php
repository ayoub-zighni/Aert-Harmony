<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class RouterController extends AbstractController
{
    #[Route('/', name: 'app_router')]
    public function index(): Response
    {
        return $this->redirectToRoute('app_produits_index');
    }
}
