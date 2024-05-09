<?php

namespace App\Controller;

use Symfony\Component\Mailer\Exception\TransportExceptionInterface;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;



class EmailController extends AbstractController
{
    
    
    #[Route('/send-email', name: 'app_sendmail')]

    public function sendEmail(MailerInterface $mailer): Response
    {
        $trasport = Transport::fromDsn('smtp://khadija.kilani@enit.utm.tn:cjnhpzddwuymoccy@smtp.gmail.com:587');

$mailer = new Mailer($trasport);
$email = (new Email());
$email->from('khadija.kilani@enit.utm.tn');
$email->to('baha.hajmabrouk2002@gmail.com');
$email->subject('Test Email');
$email->text('WSEAL EMAIL.');

$mailer->send($email);

        return new Response('Email sent successfully!');
    }
}
