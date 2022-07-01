package com.shevapro.services

//object DistanceCalculator {
//    val utils : Utils by inject()
//
//    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val theta = lon1 - lon2
//        var dist = Math.sin(utils.degreeToRadian(lat1)) * Math.sin(utils.degreeToRadian(lat2)) + Math.cos(utils.degreeToRadian(lat1)) * Math.cos(
//            utils.degreeToRadian(lat2)) * Math.cos(utils.degreeToRadian(theta))
//        dist = acos(dist)
//        dist = utils.radianToDegree(dist)
//        dist = dist * 60 * 1.1515
//        dist = dist * 1.609344
//        return dist
//    }
//}