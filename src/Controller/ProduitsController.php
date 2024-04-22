<?php

namespace App\Controller;

use App\Entity\Produits;
use App\Form\ProduitsType;
use App\Repository\ProduitsRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\FormError;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\String\Slugger\SluggerInterface;
class ProduitsController extends AbstractController
{
    #[Route('/produits', name: 'app_produits_index', methods: ['GET'])]
    public function index(ProduitsRepository $produitsRepository): Response
    {
        return $this->render('front/produits/index.html.twig', [
            'produits' => $produitsRepository->findAll(),
        ]);
    }
    #[Route('/admin/produits', name: 'admin_produits_index', methods: ['GET'])]
    public function adminIndex(ProduitsRepository $produitsRepository): Response
    {
        return $this->render('produits/index.html.twig', [
            'produits' => $produitsRepository->findAll(),
        ]);
    }

    #[Route('/produits/new', name: 'app_produits_new', methods: ['GET', 'POST'])]
    public function new(SluggerInterface $slugger,Request $request, EntityManagerInterface $entityManager): Response
    {
        $produit = new Produits();
        $form = $this->createForm(ProduitsType::class, $produit);
        $form->handleRequest($request);
        if(!$produit->getIdCategorie()  && $form->isSubmitted()){
            $form->get('idcategorie')->addError(new FormError('Categorie is required')); 
        }
        $produitsData = $request->files->get('produits');
        $brochureFile = isset($produitsData['image']) ? $produitsData['image'] : null;
        
        if(!$brochureFile && $form->isSubmitted()){
            $form->get('image')->addError(new FormError('Image is required'));
        }
        if ($form->isSubmitted() && $form->isValid()) {
            $brochureFile = $request->files->get('produits')['image'];
            if ($brochureFile) {
                $originalFilename = pathinfo($brochureFile->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$brochureFile->guessExtension();
                try {
                    $brochureFile->move(
                        $this->getParameter('uploads'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    $form->get('image')->addError(new FormError('Image is required'));
                }
                $produit->setImage($newFilename);
            }
            $entityManager->persist($produit);
            $entityManager->flush();

            return $this->redirectToRoute('admin_produits_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('produits/new.html.twig', [
            'produit' => $produit,
            'form' => $form,
        ]);
    }

    #[Route('/produits/{id}', name: 'app_produits_show', methods: ['GET'])]
    public function show(Produits $produit): Response
    {
        return $this->render('front/produits/show.html.twig', [
            'produit' => $produit,
        ]);
    }
    #[Route('/admin/produits/{id}', name: 'admin_produits_show', methods: ['GET'])]
    public function adminShow(Produits $produit): Response
    {
        return $this->render('produits/show.html.twig', [
            'produit' => $produit,
        ]);
    }

    #[Route('/produits/{id}/edit', name: 'app_produits_edit', methods: ['GET', 'POST'])]
    public function edit(SluggerInterface $slugger,Request $request, Produits $produit, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ProduitsType::class, $produit);
        $form->handleRequest($request);
       
        if(!$produit->getIdCategorie() && $form->isSubmitted()){
            $form->get('idcategorie')->addError(new FormError('Categorie is required')); 
        }
        $produitsData = $request->files->get('produits');
        $brochureFile = isset($produitsData['image']) ? $produitsData['image'] : null;
        

        if(!$brochureFile && $form->isSubmitted()){
            $form->get('image')->addError(new FormError('Image is required'));
        }
        if ($form->isSubmitted() && $form->isValid()) {
            $brochureFile = $request->files->get('produits')['image'];
            if ($brochureFile) {
                $originalFilename = pathinfo($brochureFile->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$brochureFile->guessExtension();
                try {
                    $brochureFile->move(
                        $this->getParameter('uploads'),
                        $newFilename
                    );
                } catch (FileException $e) {
                    $form->get('image')->addError(new FormError('Image is required'));
                }
                $produit->setImage($newFilename);
            }
            $entityManager->persist($produit);
            $entityManager->flush();

            return $this->redirectToRoute('admin_produits_index', [], Response::HTTP_SEE_OTHER);
        }
        
        return $this->renderForm('produits/edit.html.twig', [
            'produit' => $produit,
            'form' => $form,
        ]);
    }

    #[Route('/produits/{id}', name: 'app_produits_delete', methods: ['POST'])]
    public function delete(Request $request, Produits $produit, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$produit->getId(), $request->request->get('_token'))) {
            $entityManager->remove($produit);
            $entityManager->flush();
        }

        return $this->redirectToRoute('admin_produits_index', [], Response::HTTP_SEE_OTHER);
    }
}