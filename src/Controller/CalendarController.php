<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\EvenementsRepository;

class CalendarController extends AbstractController
{
    #[Route('/calendar/controller', name: 'app_calendar_controller')]
    public function index(EvenementsRepository $evenementsRepository)
    {
        // Récupérer tous les événements depuis le repository Evenements
        $events = $evenementsRepository->findAll();

        // Convertir les événements en un tableau JSON pour le calendrier
        $rdvs = [];

        foreach($events as $event){
            $rdvs[] = [
                'id' => $event->getId(),
                'title' => $event->getNom(),
                'start' => $event->getDate()->format('Y-m-d\TH:i:s'), // Format ISO 8601
                'end' => $event->getDate()->format('Y-m-d\TH:i:s'), // Format ISO 8601
                'description' => $event->getDescription(),
            ];
        }

        $data = json_encode($rdvs);

        // Rendre la vue Twig en transmettant les données des événements
        return $this->render('calendar/index.html.twig', compact('data'));
    }
}
