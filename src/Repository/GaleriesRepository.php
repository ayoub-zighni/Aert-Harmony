<?php

namespace App\Repository;

use App\Entity\Galeries;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;


/**
 * @extends ServiceEntityRepository<Galeries>
 *
 * @method Galeries|null find($id, $lockMode = null, $lockVersion = null)
 * @method Galeries|null findOneBy(array $criteria, array $orderBy = null)
 * @method Galeries[]    findAll()
 * @method Galeries[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class GaleriesRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Galeries::class);
    }



    public function findBySearchQuery($query)
{
    return $this->createQueryBuilder('g')
        ->andWhere('g.nom LIKE :query OR g.adresse LIKE :query OR g.ville LIKE :query OR g.pays LIKE :query')
        ->setParameter('query', '%' . $query . '%')
        ->getQuery()
        ->getResult();
}


    public function search($term)
    {
        return $this->createQueryBuilder('g')
            ->andWhere('g.nom LIKE :term')
            ->setParameter('term', '%' . $term . '%')
            ->getQuery()
            ->getResult();
    }
}
//    /**
//     * @return Galeries[] Returns an array of Galeries objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('g')
//            ->andWhere('g.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('g.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Galeries
//    {
//        return $this->createQueryBuilder('g')
//            ->andWhere('g.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }

