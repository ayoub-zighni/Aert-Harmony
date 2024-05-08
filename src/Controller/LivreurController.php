<?php
namespace App\Controller;
use App\Entity\Livreur;
use App\Form\LivreurType;
use App\Repository\LivreurRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use App\Entity\Commande;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\Form\FormView;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Writer\Xlsx;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\Component\HttpFoundation\JsonResponse; 
use Knp\Component\Pager\PaginatorInterface;
use Knp\Bundle\PaginatorBundle\Pagination\SlidingPaginationInterface;


class LivreurController extends AbstractController
{ 

    #[Route('/generate-excel', name: 'generate-excel')]
    public function generateEXCEL(LivreurRepository $livreurrepo): Response
    {
        // Récupérer les données de la base de données
        $livreurs = $livreurrepo->findAll();

        // Créer un nouveau fichier Excel
        $spreadsheet = new Spreadsheet();
        $sheet = $spreadsheet->getActiveSheet();

        // Ajouter les données de la base de données au fichier Excel
        $sheet->setCellValue('A1', 'Idlivreur');
        $sheet->setCellValue('B1', 'Nomlivreur');
        $sheet->setCellValue('C1', 'prenomlivreur');
        $sheet->setCellValue('D1', 'region');
        $sheet->setCellValue('E1', 'Email');
        $sheet->setCellValue('F1', 'telephone');
       
        // Ajoutez d'autres en-têtes de colonne au besoin

        $row = 2;
        foreach ($livreurs as $livreur) {
            $sheet->setCellValue('A' . $row, $livreur->getId());
            $sheet->setCellValue('B' . $row, $livreur->getNom());
            $sheet->setCellValue('C' . $row, $livreur->getPrenom());
            $sheet->setCellValue('D' . $row, $livreur->getRegion());
            $sheet->setCellValue('E' . $row, $livreur->getEmail());
            $sheet->setCellValue('F' . $row, $livreur->getTelephone());
            // Ajoutez d'autres valeurs de colonne au besoin

            $row++;
        }

        // Créer le fichier Excel
        $writer = new Xlsx($spreadsheet);
        $fileName = 'Livreurs.xlsx';
        $temp_file = tempnam(sys_get_temp_dir(), $fileName);
        $writer->save($temp_file);

        // Retourner le fichier Excel comme une réponse à télécharger
        return $this->file($temp_file, $fileName, ResponseHeaderBag::DISPOSITION_INLINE);
    }


   /*#[Route('/app_livreur', name: 'app_livreur', methods: ['GET'])]
    public function paginationdex(Request $request, LivreurRepository $LivreurRepository, PaginatorInterface $paginator): Response
    {
        // Retrieve all reservations
        $livreur = $LivreurRepository->findAllQuery();
      // Paginate the query
     
        // Paginate the query
        $pagination = $paginator->paginate(
            $livreur, // Query to paginate
            $request->query->getInt('page', 1), // Page number
            3 // Limit per page
        );
    
        return $this->render('livreur/read.html.twig', [
            'livreurs' => $pagination,
        ]);
    }
   */
   




    #[Route('/show-livreur', name: 'app_livreur')]
    public function index(): Response
    {
        return $this->render('livreur/index.html.twig', [
            'controller_name' => 'LivreurController',
        ]);
    }
    #[Route('/read', name: 'read')]
    public function read(LivreurRepository $livreurrepo){
        $livreur=$livreurrepo ->findAll();
        return $this->render('livreur/read.html.twig',['livreur'=>$livreur]);
    }
   /* #[Route('/tri', name: 'tri')]
    public function trierLivreurs(LivreurRepository $livreurRepository): Response
    {
        // Récupérer les livreurs triés par nom
        $livreurs = $livreurRepository->findBy([], ['nom' => 'ASC']);
    
        // Retourner les livreurs triés sous forme de réponse JSON
        return new JsonResponse($livreurs);
    } */

    #[Route('/trier', name: 'trier')]
public function tri(Request $request, LivreurRepository $LivreurRepository): Response
{
    $tri = $request->query->get('tri', 'alphabetic_asc'); // Par défaut, tri alphabétique croissant

    switch ($tri) {
        case 'alphabetic_desc':
            $livreur = $LivreurRepository->findBy([], ['nom' => 'DESC']);
            break;
        default: // Tri alphabétique croissant
            $livreur = $LivreurRepository->findBy([], ['nom' => 'ASC']);
    }

    return $this->render('livreur/read.html.twig', [
        'livreur' => $livreur,
    ]);
}
    

    #[Route('/search', name: 'app_search')]

    public function search(Request $request, LivreurRepository $livreurRepository): Response
    {
        // Retrieve search query from request
        $query = $request->query->get('query');

        // Perform search query based on the search query
        $livreur = $livreurRepository->search($query);

        // Render search results template with users data
        return $this->render('livreur/search_results.html.twig', [
            'livreur' => $livreur,
        ]);
    }




    public function sendEmail(MailerInterface $mailer, $recipient ): Response
    {
        $transport = Transport::fromDsn('smtp://khadija.kilani@enit.utm.tn:cjnhpzddwuymoccy@smtp.gmail.com:587');
    
    $mailer = new Mailer($transport);
    $email = (new Email());
    $email->from('khadija.kilani@enit.utm.tn');
    $email->to($recipient);
    $email->subject('Traitement de demande ');
    $email->text('Merci d avoir soumis vos informations pour rejoindre notre équipe. Nous vous répondrons dans les prochains jours');
    $mailer->send($email);

     return new Response();
    }
    


    #[Route('/addlivreur', name: 'addlivreur', methods: ['GET', 'POST'] )]
public function add(Request $request, EntityManagerInterface $entityManager , MailerInterface $mailer ): Response
{
    $livreur = new Livreur();
    $form = $this->createForm(LivreurType::class, $livreur);
    
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($livreur);
        $entityManager->flush();

               // Récupérer l'adresse e-mail depuis le formulaire
        $email = $livreur->getEmail();
   // Appel à la fonction d'envoi de courriel après la validation du formulaire
   $this->sendEmail($mailer, $email);

   
   // Rediriger vers la page de lecture des livreurs après l'ajout
   return $this->redirectToRoute('read');
}

return $this->render('delivery/delivery.html.twig', [
   'livreur' => $livreur,
   'form' => $form->createView(),
]);
}


#[Route('/edit/{id}', name: 'edit')]
public function edit(Request $request, $id, EntityManagerInterface $entityManager): Response
{
    // Utilisation directe de $entityManager pour récupérer le livreur
    $livreur = $entityManager->getRepository(Livreur::class)->find($id);

    // Création du formulaire avec le livreur comme données
    $form = $this->createForm(LivreurType::class, $livreur);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Mise à jour de l'entité dans la base de données
        $entityManager->flush();
        // Redirection vers la page de lecture du livreur
        return $this->redirectToRoute('read', ['id' => $id]);
    }

    // Affichage du formulaire d'édition
    return $this->render('livreur/edit.html.twig', [
        'livreur' => $livreur,
        'form' => $form->createView(),
    ]);
}


#[Route('/delet/{id}', name: 'delete')]
public function delet(ManagerRegistry $doctrine, $id)
{
    $em = $doctrine->getManager();
    $livreur = $doctrine->getRepository(Livreur::class)->find($id);

    $em->remove($livreur);
    $em->flush();

    return $this->redirectToRoute('read');
}

#[Route('/statistiques', name: 'statistiques')]
public function statistiques(LivreurRepository $livreurRepository): Response
{
    // Utiliser la méthode countLivreurByRegion pour obtenir les statistiques
    $livreursByRegion = $livreurRepository->countLivreurByRegion();

    // Passer les statistiques à la vue
    return $this->render('livreur/statistiques.html.twig', [
        'livreursByRegion' => $livreursByRegion,
    ]);
}




}
