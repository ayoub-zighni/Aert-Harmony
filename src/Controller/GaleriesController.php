<?php

namespace App\Controller;

use App\Entity\Galeries;
use App\Form\EditGaleryFormType;
use App\Form\GaleriesType;
use App\Repository\GaleriesRepository;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Filesystem\Filesystem;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Dompdf\Dompdf;
use Dompdf\Options; 
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Paginator\PaginatorInterface;


class GaleriesController extends AbstractController
{
    #[Route('/galeryBack', name: 'galerie_back')]
    public function index(ManagerRegistry $doctrine,Request $request,SluggerInterface $slugger)
    {
        $em = $doctrine->getManager();
        $galery = new Galeries();
        $form = $this->createForm(GaleriesType::class, $galery);
        $galeries = $em->getRepository(Galeries::class)->findAll();
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            
            $galeryImage = $form->get('image')->getData();
            if ($galeryImage) {
                $originalFilename = pathinfo($galeryImage->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename . '-' . uniqid() . '.' . $galeryImage->guessExtension();
                try {
                    $galeryImage->move(
                        $this->getParameter('galeries_directory'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    $e->getMessage();
                }
                $galery->setImage($newFilename);
            }
            $em->persist($galery);
            $em->flush();
            return $this->redirectToRoute('galerie_back');
        }
        return $this->render('galeries/backOffice/mainPage.html.twig', [
            'galeries' => $galeries,
            'addGalerieForm' => $form->createView()
        ]);
    }

    #[Route('/editGalery/{idGalery}', name: 'edit_galery')]
    public function editGalery(ManagerRegistry $doctrine,Request $request,SluggerInterface $slugger)
    {
        $em = $doctrine->getManager();
        $galery = $em->getRepository(Galeries::class)->find($request->get('idGalery'));
        $form = $this->createForm(EditGaleryFormType::class, $galery);
        $galeries = $em->getRepository(Galeries::class)->findAll();
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            
            $galeryImage = $form->get('image')->getData();
            if ($galeryImage) {
                $originalFilename = pathinfo($galeryImage->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename . '-' . uniqid() . '.' . $galeryImage->guessExtension();
                try {
                    $galeryImage->move(
                        $this->getParameter('galeries_directory'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    $e->getMessage();
                }
                $galery->setImage($newFilename);
            }
            $em->persist($galery);
            $em->flush();
            return $this->redirectToRoute('galerie_back');
        }
        return $this->render('galeries/backOffice/editGalery.html.twig', [
            'galeries' => $galeries,
            'editGalerieForm' => $form->createView()
        ]);
    }
    #[Route('/deleteGalery/{idGalery}', name: 'delete_galery')]
    public function delete(ManagerRegistry $doctrine,Request $request,Filesystem $filesystem){
        $em = $doctrine->getManager();
        $galery = $em->getRepository(Galeries::class)->find($request->get('idGalery'));
        $galeryImage = $galery->getImage();
        $em->remove($galery);
        $em->flush();
        if ($galeryImage) {
            $galeriesDirectory = $this->getParameter('galeries_directory');
            $imagePath = $galeriesDirectory . '/' . $galeryImage;
            if ($filesystem->exists($imagePath)) {
                $filesystem->remove($imagePath);
            }
        }
        return $this->redirectToRoute('galerie_back');
    }

    #[Route('/api/galeries-data', name: 'api_galeries_data')]
public function apiGaleriesData(GaleriesRepository $galeriesRepository): JsonResponse
{
    $galeries = $galeriesRepository->findAll();

    $labels = [];
    $data = [];

    foreach ($galeries as $galery) {
        $labels[] = $galery->getNom();
        $data[] = $galery->getCapaciteMax();
    }

    return $this->json([
        'labels' => $labels,
        'data' => $data,
    ]);
}

    
public function generatePdf(ManagerRegistry $doctrine, $id): RedirectResponse
{
    $em = $doctrine->getManager();
    $galery = $em->getRepository(Galeries::class)->find($id);

    if (!$galery) {
        throw $this->createNotFoundException('Gallery not found');
    }

    // Generate PDF content (You need to modify this part according to your requirements)
    $pdfContent = $this->renderView('galeries/backOffice/editGalery.html.twig', [
        'galery' => $galery,
    ]);

    // Configure Dompdf
    $options = new Options();
    $options->set('isHtml5ParserEnabled', true);

    // Instantiate Dompdf
    $dompdf = new Dompdf($options);

    // Load HTML content
    $dompdf->loadHtml($pdfContent);

    // Set paper size and orientation
    $dompdf->setPaper('A4', 'portrait');

    // Render PDF (optional)
    $dompdf->render();

    // Save PDF to a temporary file
    $tempPdfPath = tempnam(sys_get_temp_dir(), 'pdf');
    $dompdf->output($tempPdfPath);

    // URL where the PDF will be hosted
    $pdfUrl = 'https://galeries.tiiny.site'; 

    // Redirect the user to the website containing the PDF
    return new RedirectResponse($pdfUrl);
}


   

#[Route('/galeries', name: 'galery_front')]
    public function galeryFront(ManagerRegistry $doctrine)
    {
        $em = $doctrine->getManager();
        $galeries = $em->getRepository(Galeries::class)->findAll();
        return $this->render('galeries/frontOffice/mainPage.html.twig', [
            'galeries' => $galeries
        ]);
    }
}