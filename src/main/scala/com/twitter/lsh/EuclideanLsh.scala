package com.twitter.lsh

import com.twitter.lsh.hashing.EuclideanHashFamily
import com.twitter.lsh.stores.{HashTableManagerMemory, VectorStoreMemory}
import com.twitter.lsh.vector.{IndexedVector, VectorMath, LshVector}

/**
  * LSH Builder that uses Euclidean Distance on Regular Vectors
  * This is the best starting place when in doubt
  */
object EuclideanLsh {
  def build(lshParams: LshParams, radius: Double, vectors: Array[IndexedVector[LshVector]]) = {
    val lsh = apply(lshParams, radius)
    vectors.foreach{vec => lsh.addVector(Set(vec.id), vec.vector)}
    lsh
  }

  def apply(lshParams:LshParams, radius: Double) = {
    implicit val monoid = LshVector.lshVectorMonoid

    val hashFamily = new EuclideanHashFamily(radius, lshParams.dimensions)
    val vectorStore = VectorStoreMemory[Long, LshVector]
    val hashTableManager = new HashTableManagerMemory[Long, LshVector](hashFamily, lshParams.hashTables, lshParams.hashFunctions)
    new Lsh[Long, LshVector](hashFamily, vectorStore, hashTableManager, VectorMath.normalize)
  }
}
