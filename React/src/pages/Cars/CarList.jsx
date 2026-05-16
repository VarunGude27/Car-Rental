import { useState, useEffect, useMemo } from "react";
import { Link } from "react-router-dom";
import axiosInstance from "../../api/axios";
import "./CarList.css";

const fetchAllCars = async () => {
  try {
    const response = await axiosInstance.get("/view/cars");

    console.log("Cars API Response:", response.data);

    // Handle different response structures
    const carsArray = Array.isArray(response.data)
      ? response.data
      : response.data.content || response.data.cars || [];

    return carsArray;
  } catch (error) {
    console.error("Error fetching cars:", error);
    return [];
  }
};

const getStoredValue = (key, defaultValue) => {
  const storedValue = localStorage.getItem(key);
  return storedValue ? JSON.parse(storedValue) : defaultValue;
};

const CarList = () => {
  const [cars, setCars] = useState([]);
  const [carImages, setCarImages] = useState({});
  const [error, setError] = useState(null);

  const [makes, setMakes] = useState([]);
  const [categories, setCategories] = useState([]);
  const [makeModelMap, setMakeModelMap] = useState({});

  const [make, setMake] = useState(() => getStoredValue("make", ""));
  const [model, setModel] = useState(() => getStoredValue("model", ""));
  const [category, setCategory] = useState(() =>
    getStoredValue("category", "")
  );
  const [searchTerm, setSearchTerm] = useState(() =>
    getStoredValue("searchTerm", "")
  );

  const [currentPage, setCurrentPage] = useState(1);

  const itemsPerPage = 9;

  useEffect(() => {
    const loadCars = async () => {
      try {
        const data = await fetchAllCars();

        setCars(data);

        const uniqueMakes = [...new Set(data.map((car) => car.brand))];

        const uniqueCategories = [
          ...new Set(data.map((car) => car.category)),
        ];

        const modelMap = {};

        data.forEach((car) => {
          if (!modelMap[car.brand]) {
            modelMap[car.brand] = [];
          }

          if (!modelMap[car.brand].includes(car.model)) {
            modelMap[car.brand].push(car.model);
          }
        });

        setMakes(uniqueMakes);
        setCategories(uniqueCategories);
        setMakeModelMap(modelMap);

        fetchCarImages(data);
      } catch (err) {
        console.error(err);
        setError("Failed to load cars.");
      }
    };

    loadCars();
  }, []);

  const fetchCarImages = async (carsList) => {
    const images = {};

    await Promise.all(
      carsList.map(async (car) => {
        try {
          const response = await axiosInstance.get(
            `/view/cars/${car.id}/image`,
            {
              responseType: "blob",
            }
          );

          images[car.id] = URL.createObjectURL(response.data);
        } catch (error) {
          images[car.id] =
            "https://dummyimage.com/400x300/000/fff";
        }
      })
    );

    setCarImages(images);
  };

  const filteredCars = useMemo(() => {
    if (!Array.isArray(cars)) return [];

    return cars.filter((car) => {
      const search = searchTerm.toLowerCase();

      return (
        (`${car.brand} ${car.model}`
          .toLowerCase()
          .includes(search) ||
          car.category?.toLowerCase().includes(search)) &&
        (make === "" || car.brand === make) &&
        (model === "" || car.model === model) &&
        (category === "" || car.category === category)
      );
    });
  }, [cars, searchTerm, make, model, category]);

  const totalPages = Math.ceil(filteredCars.length / itemsPerPage);

  const currentCars = filteredCars.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <div className="car-list">

      <h1>Car List</h1>

      {error && <p>{error}</p>}

      <input
        type="text"
        placeholder="Search Cars"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />

      <select
        value={make}
        onChange={(e) => {
          setMake(e.target.value);
          setModel("");
        }}
      >
        <option value="">All Makes</option>

        {makes.map((make, index) => (
          <option key={index} value={make}>
            {make}
          </option>
        ))}
      </select>

      <select
        value={model}
        onChange={(e) => setModel(e.target.value)}
      >
        <option value="">All Models</option>

        {make
          ? makeModelMap[make]?.map((model, index) => (
              <option key={index} value={model}>
                {model}
              </option>
            ))
          : []}
      </select>

      <select
        value={category}
        onChange={(e) => setCategory(e.target.value)}
      >
        <option value="">All Categories</option>

        {categories.map((cat, index) => (
          <option key={index} value={cat}>
            {cat}
          </option>
        ))}
      </select>

      <h3>Total Cars: {filteredCars.length}</h3>

      <div className="car-cards">
        {currentCars.map((car) => (
          <Link
            key={car.id}
            to={`/cars/${car.id}`}
            className="car-card-link"
          >
            <div className="car-card">

              <img
                src={
                  carImages[car.id] ||
                  "https://dummyimage.com/400x300/000/fff"
                }
                alt={car.brand}
              />

              <div className="car-info">
                <h3>
                  {car.brand} {car.model}
                </h3>

                <p>{car.category}</p>

                <p>${car.pricePerDay}/day</p>
              </div>

            </div>
          </Link>
        ))}
      </div>

      <div className="pagination">
        <button
          disabled={currentPage === 1}
          onClick={() =>
            setCurrentPage((prev) => prev - 1)
          }
        >
          Prev
        </button>

        <span>
          {currentPage} / {totalPages || 1}
        </span>

        <button
          disabled={currentPage === totalPages}
          onClick={() =>
            setCurrentPage((prev) => prev + 1)
          }
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default CarList;