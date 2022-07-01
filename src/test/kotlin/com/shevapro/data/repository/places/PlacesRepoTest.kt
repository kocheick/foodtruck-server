package com.shevapro.data.repository.places
//
//import com.shevapro.data.models.Address
//import com.shevapro.data.models.Coordinates
//import com.shevapro.data.models.Place
//import com.shevapro.data.models.Position
//import com.shevapro.data.places
//import com.shevapro.data.repository.db.DatabaseFactory
//import kotlinx.coroutines.runBlocking
//import org.junit.jupiter.api.*
//
//import org.junit.jupiter.api.Assertions.*
//import java.util.*
//import kotlin.test.assertContains
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//internal class PlacesRepoTest {
//
//    lateinit var testDB: PlacesRepository
//
//    @BeforeAll
//    fun setUp() {
//        DatabaseFactory.init()
//        testDB = PlacesRepoImpl()
//        DatabaseFactory.resetTables()
//        DatabaseFactory.recreateTables()
//    }
//
//
//    @AfterEach
//    fun tearDown() {
//        DatabaseFactory.resetTables()
//        DatabaseFactory.recreateTables()
//    }
//
//    @AfterAll
//    fun resetDB() {
//        DatabaseFactory.resetTables()
//    }
//
//    @Test
//    fun `add an item`() {
//        runBlocking {
//            val initialTotal = testDB.getPlaces().size
//
//            testDB.add("Bon", "Jour", "SOUTH_WEST", 54.0, 34.4)
//            val newTotal = testDB.getPlaces().size
//
//            testDB
//            assertEquals(initialTotal + 1, newTotal)
//
//        }
//
//    }
//
//    @Test
//    fun `add two items to repository`() {
//        runBlocking {
//            val initialTotal = testDB.getPlaces().size
//
//            testDB.add("Bon", "Jour", "SOUTH_EAST", 54.0, 34.4)
//            testDB.add("Bonr", "Jour", "SOUTH_WEST", 54.0, 34.4)
//
//            val newTotal = testDB.getPlaces().size
//
//            assertEquals(initialTotal + 2, newTotal)
//
//        }
//
//    }
//
//
//    @Test
//    fun `remove an item by id`() {
//        runBlocking {
//            places.forEach {
//
//                testDB.add(
//                    it.address.street, it.address.crossStreet,
//                    it.address.streetPosition!!.name, it.coordinates.latitude, it.coordinates.longitude
//                )
//            }
//            val items = testDB.getPlaces()
//            val initialItemSize = items.size
//            val itemToDelete = testDB.getPlaceById(3)
//            assertContains(items, itemToDelete)
//
//
//            testDB.removeById(3)
//            val deletedItem = testDB.getPlaceById(3)
//            assertNull(deletedItem)
//
//            val finalItemsSize = testDB.getPlaces().size
//            assertEquals(initialItemSize.minus(1), finalItemsSize )
//
//
//        }
//
//    }
//
//    @Test
//    fun `update an item`() {
//        val initialPlace = Place(UUID.fromString("8"), Coordinates(39.3, 39.3), Address("LA", "Bon", Position.valueOf("NORD_WEST")))
//        runBlocking {
//
//            testDB.add(
//                initialPlace.address.street,
//                initialPlace.address.crossStreet,
//                initialPlace.address.streetPosition!!.name,
//                initialPlace.coordinates.latitude,
//                initialPlace.coordinates.longitude
//            )
//
//            val updatedPlace = initialPlace.copy(
//                coordinates = Coordinates(0.0, 99.00),
//                address = Address("Jazz", "LOLU", Position.valueOf("NORD_EAST"))
//            )
//
//
//            testDB.update(
//                updatedPlace.id,
//                updatedPlace.address.street,
//                updatedPlace.address.crossStreet,
//                updatedPlace.address.streetPosition!!.name,
//                updatedPlace.coordinates.latitude,
//                updatedPlace.coordinates.longitude
//            )
//
//
//            val databseItem = testDB.getPlaceById(initialPlace.id)
//
//            assertEquals(initialPlace.id, databseItem?.id)
//            assertEquals(databseItem, updatedPlace)
//            assertNotEquals(databseItem?.address?.street, initialPlace.address.street)
//
//        }
//    }
//
//    @Test
//    fun `get all item `() {
//    }
//
//    @Test
//    fun clearPlaces() {
//    }
//}