<?php

namespace App\Controller;

use App\Entity\Evenements;
use App\Form\EvenementsType;
use App\Services\TwilioService;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Filesystem\Filesystem;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;
use Twilio\Rest\Client;
use Doctrine\Persistence\ManagerRegistry;
use App\Form\EditEventFormType;
use Symfony\Component\HttpFoundation\JsonResponse;



class EvenementsController extends AbstractController
{
    #[Route('/eventBack/{idGalery}', name: 'event_back')]
    public function index(ManagerRegistry $doctrine, Request $request, SluggerInterface $slugger, TwilioService $twilioService)
    {
        $em = $doctrine->getManager();
        $idGalery = $request->get('idGalery');
        $event = new Evenements();
        $form = $this->createForm(EvenementsType::class, $event);
        $events = $em->getRepository(Evenements::class)->joinGalery($idGalery);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $eventImage = $form->get('image')->getData();
            if ($eventImage) {
                $originalFilename = pathinfo($eventImage->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename . '-' . uniqid() . '.' . $eventImage->guessExtension();
                try {
                    $eventImage->move(
                        $this->getParameter('events_directory'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    $e->getMessage();
                }
                $event->setImage($newFilename);
            }
            $event->setGalerieid($idGalery);
            $em->persist($event);
            $em->flush();

            // Send Twilio message
            $twilioMessage = "New event added"; // Change this message as needed
            $twilioService->sendTwillioMessage("+21696740740", $twilioMessage);

            return $this->redirectToRoute('event_back', ['idGalery' => $idGalery, 'events' => $events]);
        }

        return $this->render('evenements/backOffice/mainPage.html.twig', [
            'events' => $events,
            'addEventForm' => $form->createView()
        ]);
    }


    #[Route('/editEvent/{idGalery}/{idEvent}', name: 'edit_event')]
    public function editEvent(ManagerRegistry $doctrine,Request $request,SluggerInterface $slugger)
    {
        $em = $doctrine->getManager();
        $idGalery = $request->get('idGalery');
        $event = $em->getRepository(Evenements::class)->find($request->get('idEvent'));
        $form = $this->createForm(EditEventFormType::class, $event);
        $events = $em->getRepository(Evenements::class)->joinGalery($idGalery);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $eventImage = $form->get('image')->getData();
            if ($eventImage) {
                $originalFilename = pathinfo($eventImage->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename . '-' . uniqid() . '.' . $eventImage->guessExtension();
                try {
                    $eventImage->move(
                        $this->getParameter('events_directory'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    $e->getMessage();
                }
                $event->setImage($newFilename);
            }
            $event->setGalerieid($idGalery);
            $em->persist($event);
            $em->flush();
            return $this->redirectToRoute('event_back',['idGalery' => $idGalery,'events' => $events]);
        }
        return $this->render('evenements/backOffice/editEvent.html.twig', [
            'events' => $events,
            'editEventForm' => $form->createView()
        ]);
    }
    #[Route('/deleteEvent/{idGalery}/{idEvent}', name: 'delete_event')]
    public function deleteEvent(ManagerRegistry $doctrine,Request $request,Filesystem $filesystem){
        $em = $doctrine->getManager();
        $event = $em->getRepository(Evenements::class)->find($request->get('idEvent'));
        $eventImage = $event->getImage();
        $em->remove($event);
        $em->flush();
        if ($eventImage) {
            $galeriesDirectory = $this->getParameter('events_directory');
            $imagePath = $galeriesDirectory . '/' . $eventImage;
            if ($filesystem->exists($imagePath)) {
                $filesystem->remove($imagePath);
            }
        }
        $idGalery = $request->get('idGalery');
        $events = $em->getRepository(Evenements::class)->joinGalery($idGalery);
        return $this->redirectToRoute('event_back',[
            'idGalery' => $idGalery,
            'events' => $events
        ]);
    }


    #[Route('/map', name: 'map')]
    public function showMap(): Response
    {
        return $this->render('map/mainPage.html.twig');
    }


    #[Route('/events/{idGalery}', name: 'event_front')]
    public function eventFront(ManagerRegistry $doctrine,Request $request)
    {
        $em = $doctrine->getManager();
        $idGalery = $request->get('idGalery');
        $event = new Evenements();
        $events = $em->getRepository(Evenements::class)->joinGalery($idGalery);
        return $this->render('evenements/frontOffice/mainPage.html.twig', [
            'events' => $events
        ]);
    }
}
