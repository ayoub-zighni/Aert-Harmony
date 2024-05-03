<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\User;
use League\OAuth2\Client\Provider\AbstractProvider;
use League\OAuth2\Client\Provider\Facebook;

class HomeController extends AbstractController
{
    private AbstractProvider $provider;

    public function __construct()
    {
        $this->provider = new Facebook([
            'clientId' => $_ENV['FCB_ID'],
            'clientSecret' => $_ENV['FCB_SECRET'],
            'redirectUri' => $_ENV['FCB_CALLBACK'],
            'graphApiVersion' => 'v15.0',
        ]);
    }
    #[Route('/home', name: 'app_home')]
    public function index(): Response
    {
        return $this->render('home/index.html.twig', [
            'controller_name' => 'HomeController',
        ]);
    }

    #[Route('/back', name: 'app_back')]
    public function back(): Response
    {
        return $this->render('home/back.html.twig', [
            'controller_name' => 'HomeController',
        ]);
    }
    #[Route('/', name: 'app_front')]
    public function front(): Response
    {
        return $this->render('home/front.html.twig', [
            'controller_name' => 'HomeController',
        ]);
    }

    #[Route('/fcb-login', name: 'fcb_login')]
    public function fcbLogin(): Response
    {

        $helper_url=$this->provider->getAuthorizationUrl();

        return $this->redirect($helper_url);
    }
    #[Route('/fcb-callback', name: 'fcb_callback')]
    public function fcbCallBack(UserRepository $userDb, EntityManagerInterface $manager): Response
    {
        //Récupérer le token
        $token = $this->provider->getAccessToken('authorization_code', [
            'code' => $_GET['code']
        ]);

        try {
            //Récupérer les informations de l'utilisateur
            $user = $this->provider->getResourceOwner($token)->toArray();

            $email = $user['email'];
            $nom = $user['name'];

            $birthdate = null;
            if (isset($user['birthday'])) {
                $birthdate = \DateTime::createFromFormat('m/d/Y', $user['birthday']);
            } elseif (isset($user['birth_date'])) {
                $birthdate = new \DateTime($user['birth_date']);
            }

            // Check if the provider provides the role directly as 'role'
            $role = $user['role'] ?? ['user']; // Initialize role as ['user'] if not provided

            $prenom = $user['prenom'] ?? null;
            // If 'prenom' is not available, try to extract it from the full name
            if (!$prenom) {
                $fullName = explode(' ', $nom, 2);
                $prenom = $fullName[0] ?? null;
            }
            // If 'username' is not available, generate a username based on the email
            $username = $user['username'] ?? null;
            if (!$username) {
                // Extract the username from the email
                $emailParts = explode('@', $email);
                $username = $emailParts[0] ?? null;
            }

            //Vérifier si l'utilisateur existe dans la base des données
            $user_exist = $userDb->findOneByEmail($email);

            if ($user_exist) {
                $user_exist->setNom($nom);
                $manager->flush();

                return $this->render('home/front.html.twig', [
                    'username' => $username,
                ]);
            } else {
                $new_user = new User();

                $new_user->setNom($nom)
                    ->setPrenom($prenom)
                    ->setUsername($username)
                    ->setEmail($email)
                    ->setPassword(sha1(str_shuffle('abscdop123390hHHH;:::OOOI')))
                    ->setBirthdate($birthdate) // Set the birthDate
                    ->setRoles($role); // Set the role

                $manager->persist($new_user);
                $manager->flush();

                return $this->render('home/front.html.twig', [
                    'title' => 'welcome',
                ]);
            }



        } catch (\Throwable $th) {
            dd($th->getMessage());
        }
        }
}
