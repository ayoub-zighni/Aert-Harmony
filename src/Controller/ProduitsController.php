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
use Endroid\QrCode\Writer\PngWriter;
use Endroid\QrCode\Builder\BuilderInterface; 
use Endroid\QrCode\Writer\Result\PngResult;
use Infobip\Configuration;
use Infobip\Api\SendSmsApi;
use Knp\Component\Pager\PaginatorInterface;
use Infobip\Model\SmsDestination;
use Infobip\Model\SmsTextualMessage;
use Infobip\Model\SmsAdvancedTextualRequest;




class ProduitsController extends AbstractController
{
    private $qrCodeBuilder;

    public function __construct(BuilderInterface $qrCodeBuilder)
    {
        $this->qrCodeBuilder = $qrCodeBuilder;
    }
    
    #[Route('/produits', name: 'app_produits_index', methods: ['GET'])]
    public function index(ProduitsRepository $produitsRepository): Response
    {
        $produits = $produitsRepository->findAll();

        foreach ($produits as $produit) {
            // Check if $this->qrCodeBuilder is not null
            if ($this->qrCodeBuilder !== null) {
                // Customize the QR code data
                $qrCodeResult = $this->qrCodeBuilder
                    ->data($produit->getNom())
                    ->build();

                // Convert the QR code result to a string representation
                $qrCodeString = $this->convertQrCodeResultToString($qrCodeResult);

                // Add the QR code string to the  entity
                $produit->setQrCode($qrCodeString);
            }
        }
        return $this->render('front/produits/index.html.twig', [
            'produits' => $produits,
        ]);
    }
    private function convertQrCodeResultToString(PngResult $qrCodeResult): string
    {
        // Convert the result to a string (e.g., base64 encode the image)
        // Adjust this logic based on how you want to represent the QR code data
        return 'data:image/png;base64,' . base64_encode($qrCodeResult->getString());
    }
    #[Route('/admin/produits', name: 'admin_produits_index', methods: ['GET'])]
    public function adminIndex(Request $request, ProduitsRepository $produitsRepository, PaginatorInterface $paginator): Response
    {
        $queryBuilder = $produitsRepository->createQueryBuilder('p');
    
        // Create a query to fetch all products
        $query = $queryBuilder->getQuery();
    
        // Paginate the results
        $pagination = $paginator->paginate(
            $query, /* query NOT result */
            $request->query->getInt('page', 1), /* page number */
           5 /* limit per page */
        );
    
        return $this->render('produits/index.html.twig', [
            'pagination' => $pagination,
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
            
            $this->sendVerificationSMS('+21692492396');
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
    public function show(ProduitsRepository $produitsRepository,$id): Response
    { 
        $produits=$produitsRepository->find($id);

        return $this->render('front/produits/show.html.twig', [
            'produit' => $produits
        ]);
    }
    #[Route('/admin/produits/{id}', name: 'admin_produits_show', methods: ['GET'])]
    public function adminShow(ProduitsRepository $produitsRepository,$id): Response
    {
        $produit=$produitsRepository->find($id);

        return $this->render('produits/show.html.twig', [
            'produit' => $produit,
        ]);
    }

    #[Route('/produits/{id}/edit', name: 'app_produits_edit', methods: ['GET', 'POST'])]
    public function edit(SluggerInterface $slugger,Request $request,ProduitsRepository $produitsRepository,$id, EntityManagerInterface $entityManager): Response
    {
        $produit=$produitsRepository->find($id);

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
    public function delete(Request $request, ProduitsRepository $produitsRepository,$id, EntityManagerInterface $entityManager): Response
    {
        $produit=$produitsRepository->find($id);

        if ($this->isCsrfTokenValid('delete'.$produit->getId(), $request->request->get('_token'))) {
            $entityManager->remove($produit);
            $entityManager->flush();
        }

        return $this->redirectToRoute('admin_produits_index', [], Response::HTTP_SEE_OTHER);
    }
    private function sendVerificationSMS($phoneNumber): Response
    {
        $baseurl = $this->getParameter("sms_gateway.baseurl");
        $apikey = $this->getParameter("sms_gateway.apikey");
        $apikeyPrefix = $this->getParameter("sms_gateway.apikeyprefix");
        
        $configuration = (new Configuration())
            ->setHost($baseurl)
            ->setApiKeyPrefix('Authorization', $apikeyPrefix)
            ->setApiKey('Authorization', $apikey);

        $client = new \GuzzleHttp\Client();
        $sendSmsApi = new SendSMSApi($client, $configuration);
        
        // 2. Create message object with destination
        $destination = (new SmsDestination())->setTo('+21692492396');
$message = (new SmsTextualMessage())
    ->setFrom('Art & Harmonie')
    ->setText(' Consultez nos nouvaux offres via nos sitweb ,equipe de support ') 
    ->setDestinations([$destination]);
        
        // 3. Create message request with all the messages that you want to send
        $request = (new SmsAdvancedTextualRequest())->setMessages([$message]);
        
        // 4. Send !
        try {
            $smsResponse = $sendSmsApi->sendSmsMessage($request);
            
            dump($smsResponse);
        } catch (\Throwable $apiException) {
            // HANDLE THE EXCEPTION
            dump($apiException);
        }
        
        return new Response("Success (?)");
    }
    
}
