package com.moqochallenge.poi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.repository.POIRepository
import com.moqochallenge.poi.presentation.viewmodel.POIViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*


@ExperimentalCoroutinesApi
class POIViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Ensures LiveData updates immediately

    private lateinit var viewModel: POIViewModel
    private lateinit var repository: POIRepository
    private lateinit var api: FakePOIApi
    private lateinit var dao: FakePOIDao

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        api = FakePOIApi() // Use Fake API
        dao = FakePOIDao() // Use Fake DAO
        repository = POIRepository(api, dao) // Use real repository with fakes
        viewModel = POIViewModel(repository)

        Dispatchers.setMain(testDispatcher) // Set test dispatcher
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset dispatcher after each test
    }

    // Test API Call for `loadPOIs()`
    @Test
    fun `loadPOIs should fetch POIs from API and update poiList`() = runTest {
        val fakePOIs = listOf(
            POI(id = "1", latitude = 52.5200, longitude = 13.4050, name = "POI1", positionType = "standalone", vehicleType = "car", latestParkingId = null, appRelation = "native"),
            POI(id = "2", latitude = 48.8566, longitude = 2.3522, name = "POI2", positionType = "parking", vehicleType = "car", latestParkingId = 123, appRelation = "native")
        )

        api.addPOIs(*fakePOIs.toTypedArray()) // Add Fake POIs to Fake API

        viewModel.loadPOIs(52.52, 13.40, 52.30, 13.20)
        advanceUntilIdle() // Ensure coroutine execution completes

        assertEquals(fakePOIs, viewModel.poiList.first()) // Verify poiList is updated
        assertEquals(false, viewModel.isLoading.first()) // isLoading should be false after fetching
        assertNull(viewModel.errorMessage.first()) // No error should be present
    }

    // Test API Call Failure in `loadPOIs()`
    @Test
    fun `loadPOIs should return empty list when API fails`() = runTest {
        viewModel.loadPOIs(52.52, 13.40, 52.30, 13.20)
        advanceUntilIdle()

        assertEquals(emptyList<POI>(), viewModel.poiList.first()) // POI list should be empty
        assertEquals(false, viewModel.isLoading.first()) // isLoading should be false
        assertNull(viewModel.errorMessage.first()) // No error should be present
    }

    // Test `calculateBoundingBox()`
    @Test
    fun `calculateBoundingBox should return correct coordinates`() {
        val location = LatLng(52.52, 13.40)
        val distanceInKm = 10.0

        val (ne, sw) = viewModel.calculateBoundingBox(location, distanceInKm)

        assertEquals(52.61, ne.latitude, 0.1) // Check NE latitude
        assertEquals(13.52, ne.longitude, 0.1) // Check NE longitude
        assertEquals(52.43, sw.latitude, 0.1) // Check SW latitude
        assertEquals(13.28, sw.longitude, 0.1) // Check SW longitude
    }
}
