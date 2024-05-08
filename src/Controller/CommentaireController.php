<?php

namespace App\Controller;

use App\Entity\Commentaire;
use App\Entity\Produits;
use App\Form\CommentaireType;
use App\Repository\CommentaireRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/commentaire')]  
class CommentaireController extends AbstractController
{
    #[Route('/', name: 'app_commentaire_index', methods: ['GET'])]
    public function index(CommentaireRepository $commentaireRepository): Response
    {
        return $this->render('commentaire/index.html.twig', [
            'commentaires' => $commentaireRepository->findAll(),
        ]);
    }

    #[Route('/new/{id}', name: 'app_commentaire_new', methods: ['GET', 'POST'])]
    public function new(Produits $produit,Request $request, EntityManagerInterface $entityManager): Response
    {
        $commentaire = new Commentaire();

            $commentaire->setCreatedAt(new \DateTimeImmutable());
            $commentaire->setMessage($request->get('message'));
            $commentaire->setProduit($produit);
            $entityManager->persist($commentaire);
            $entityManager->flush();

            return $this->redirectToRoute('app_produits_show', ['id' => $produit->getId()], Response::HTTP_SEE_OTHER);
        
    }

    // #[Route('/{id}', name: 'app_commentaire_show', methods: ['GET'])]
    // public function show(Commentaire $commentaire): Response
    // {
    //     return $this->render('commentaire/show.html.twig', [
    //         'commentaire' => $commentaire,
    //     ]);
    // }

    #[Route('/{id}/edit', name: 'app_commentaire_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Commentaire $commentaire, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(CommentaireType::class, $commentaire);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_commentaire_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('commentaire/edit.html.twig', [
            'commentaire' => $commentaire,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_commentaire_delete', methods: ['GET','POST'])]
    public function delete(Request $request, Commentaire $commentaire, EntityManagerInterface $entityManager): Response
    {
            $id = $commentaire->getProduit()->getId();
            $entityManager->remove($commentaire);
            $entityManager->flush();
            return $this->redirectToRoute('app_produits_show', ['id' => $id], Response::HTTP_SEE_OTHER);
    }
}
