<?php

namespace App\Controller;

use App\Entity\Commande;
use App\Form\CommandeType;
use App\Repository\CommandeRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Dompdf\Dompdf;
use Dompdf\Options;



#[Route('/commande')]
class CommandeController extends AbstractController
{
 
    #[Route('/', name: 'app_commande_index', methods: ['GET'])]
    public function index(CommandeRepository $commandeRepository): Response
    {
        return $this->render('commande/index.html.twig', [
            'commande' => $commandeRepository->findAll(),
        ]);
    }


    function generatePDF($adrlivraison, $num2) 
    {
        $destination = realpath(__DIR__ . '/../../public/pdf');
        // Include autoloader
        require __DIR__ . "/../../vendor/autoload.php";
    
        $html = '<html><body>';
    
        // Ajouter le chemin vers le logo
        $logoPath = 'C:\Users\bahah\Desktop\Projet\SymfonyPIDEV\public\images\LOGO.png';
        
        // Ajouter l'image du logo
        if (file_exists($logoPath)) {
            $imageData = file_get_contents($logoPath);
            $imageBase64 = base64_encode($imageData);
            $html .= '<img src="data:image/png;base64,' . $imageBase64 . '" alt="ArtetHarmonie Logo" style="display: block; margin: auto; width: 200px; height: auto;">';
        } else {
            $html .= '<p>Logo not found</p>'; // Afficher un message si le logo n'est pas trouvé
        }
    
        // Autres éléments HTML
        $html .= '<h2 style="color: #336699; font-weight: bold; text-decoration: underline;">Equipe art et harmonie </h2>';
        $html .= '<p style="font-weight: bold;">Bienvenue</p>';
        $html .= '<p><strong>Email:</strong> artetharmonie@gmail.com</p>';
        $html .= '<p><strong>Phone number:</strong> +216 94 568 236</p>';
        $html .= '<p style="font-style: italic;">Contact us!</p>';
    
        $html .= '<h1 style="color: green; font-weight: bold; text-decoration: underline; text-align: center;">Your Order Details</h1>'; // Title
        $html .= '<p style="font-weight: bold; font-style: italic; text-align: center;">Thank you for choosing us, and we look forward to welcoming you. !</p>'; // Comment
    
        $html .= "<p style='text-align: center;'><span style='color: blue; text-decoration: underline;'>Adresse de Livraison :</span> $adrlivraison </p>"; // Adresse de Livraison
        $html .= "<p style='text-align: center;'><span style='color: blue; text-decoration: underline;'>Numéro de Téléphone :</span> $num2</p>"; // Numéro de Téléphone
    
        $html .= '</body></html>';
    
        $dompdf = new Dompdf();
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'portrait');
        $dompdf->render();
    
        $pdfContent = $dompdf->output();
    
        $response = new Response($pdfContent);
        $response->headers->set('Content-Type', 'application/pdf');
        $response->headers->set('Content-Disposition', 'attachment; filename="détails de la commande.pdf"');
    
        return $response;
    }
    

   #[Route('/download-pdf', name: 'app_commande_download_pdf')]
    public function downloadPDF(): Response
    {
        
        // Créer la réponse HTTP avec le contenu du PDF
        $response = new Response($pdfContent);
        $response->headers->set('Content-Type', 'application/pdf');
        $response->headers->set('Content-Disposition', 'attachment; filename="détails de la commande.pdf"');
    
        return $response;
    }



    #[Route('/new', name: 'app_commande_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $commande = new Commande();
        $form = $this->createForm(CommandeType::class, $commande);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($commande);
            $entityManager->flush();
    
            $adresseLivraison = $commande->getAdresseLivraison();
            $numTel = $commande->getNumTel();
        
            // Générer le PDF après avoir persisté la commande
            try {
             $pdfResponse = $this->generatePDF($adresseLivraison, $numTel);
             return $pdfResponse;
            } catch (\Exception $e) {
                // Gérer les exceptions ici
                $this->addFlash('error', 'Une erreur est survenue lors de la génération du PDF.');
                return $this->redirectToRoute('app_commande_index');
            }
            
            return $this->redirectToRoute('app_commande_index');
        }
    
        return $this->renderForm('commande/new.html.twig', [
            'commande' => $commande,
            'form' => $form,
        ]);
    }
    

    #[Route('/{idC}/show', name: 'app_commande_show', methods: ['GET'])]
    public function show(CommandeRepository $commandeRepository,$idC): Response
    {
        $commande=$commandeRepository->find($id);
        return $this->render('commande/show.html.twig', [
            'commande' => $commande,
        ]);
    }

    #[Route('/{idC}/edit', name: 'app_commande_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Commande $commande, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(CommandeType::class, $commande);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_commande_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('commande/edit.html.twig', [
            'commande' => $commande,
            'form' => $form,
        ]);
    }

    #[Route('/delete/{idC}', name: 'app_commande_delete', methods: ['GET','POST'])]
    public function delete(Request $request, Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$commande->getIdC(), $request->request->get('_token'))) {
            $entityManager->remove($commande);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_commande_index', [], Response::HTTP_SEE_OTHER);
    }
    
    
}
