import { useState, useEffect } from "react";
import "./PopularCars.css";
import axiosInstance from "../../api/axios";
import { useNavigate } from "react-router-dom";

const PopularCars = () => {

  const [cars, setCars] = useState([]);
  const [filter, setFilter] = useState("All");
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {

    const fetchCars = async () => {

      try {

        const response = await axiosInstance.get("/view/cars");

        console.log("Popular Cars Response:", response.data);

        const carsArray = Array.isArray(response.data)
          ? response.data
          : response.data.content || response.data.cars || [];

        const carsWithImages = await Promise.all(
          carsArray.map(async (car) => {

            const imageSrc = await fetchImage(car.id);

            return {
              ...car,
              image: imageSrc,
            };

          })
        );

        setCars(carsWithImages);

      } catch (error) {

        console.error("Error fetching cars:", error);

      } finally {

        setLoading(false);

      }
    };

    fetchCars();

  }, []);

  const fetchImage = async (carId) => {

    try {

      const response = await axiosInstance.get(
        `/view/cars/${carId}/image`,
        {
          responseType: "blob",
        }
      );

      return URL.createObjectURL(response.data);

    // eslint-disable-next-line no-unused-vars
    } catch (error) {

      return "https://dummyimage.com/400x300/000/fff";
    }
  };

  const filteredCars =
    filter === "All"
      ? cars
      : cars.filter((car) => car.category === filter);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="popular-cars">

      <h2 className="header">Featured Cars</h2>

      <div className="car-filters">

        {[
          "All",
          "SEDAN",
          "SUV",
          "COUPE",
          "CONVERTIBLE",
          "TRUCK",
          "VAN",
          "OTHER",
        ].map((category) => (

          <button
            key={category}
            className={
              filter === category
                ? "filter-button active"
                : "filter-button"
            }
            onClick={() => setFilter(category)}
          >
            {category}
          </button>

        ))}

      </div>

      <div className="car-grid">

        {filteredCars.map((car) => (

          <div
            key={car.id}
            className="car-card-popular"
            onClick={() => navigate(`/cars/${car.id}`)}
          >

            <img src={car.image} alt={car.brand} />

            <div className="car-info">

              <h3>
                {car.brand} {car.model}
              </h3>

              <p>{car.category}</p>

              <h4>${car.pricePerDay}/day</h4>

            </div>

          </div>

        ))}

      </div>

    </div>
  );
};

export default PopularCars;