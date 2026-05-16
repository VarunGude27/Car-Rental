import React, { useState } from "react";
import "./ContactUs.css";
import { FaPhone, FaEnvelope, FaMapMarkerAlt } from "react-icons/fa";


const ContactUs = () => {
    const [formData, setFormData] = useState({ name: "", email: "", message: "" });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [feedback, setFeedback] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        setTimeout(() => {
            setFeedback("Your message has been sent successfully!");
            setFormData({ name: "", email: "", message: "" });
            setIsSubmitting(false);
        }, 2000);
    };

    return (
        <>
        <div className="contact-us">
            <div className="contact-left">
                <h2>Contact Us</h2>
                <div className="contact-item">
                    <FaPhone className="icon" />
                    <span>+95 (09)123456789 </span>
                </div>
                <div className="contact-item">
                    <FaEnvelope className="icon" />
                    <span>mzp@gmail.com</span>
                </div>
                <div className="contact-item">
                    <FaMapMarkerAlt className="icon" />
                    <span>Mandalay, Myanmar</span>
                </div>
                
                <div className="map-container">
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3700.913714060264!2d96.0893972743334!3d21.937868355936512!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x30cb6d61168aa7c1%3A0xcf1acb8f196c3bd6!2sMyanmar%20Institute%20of%20Information%20Technology!5e0!3m2!1sen!2smm!4v1736305051483!5m2!1sen!2smm" width="100%" height="300" style={{ border: 0 }} allowFullScreen="" loading="lazy" referrerPolicy="no-referrer-when-downgrade"></iframe>
                </div>
            </div>

            <div className="contact-right">
                <form onSubmit={handleSubmit} className="contact-form">
                    <label htmlFor="name">Your Name</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />

                    <label htmlFor="email">Your Email</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />

                    <label htmlFor="message">Your Message</label>
                    <textarea
                        id="message"
                        name="message"
                        rows="5"
                        value={formData.message}
                        onChange={handleChange}
                        required
                    ></textarea>

                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? "Sending..." : "Send"}
                    </button>
                </form>
                {feedback && <p className="feedback-message">{feedback}</p>}
            </div>
        </div>
        </>
    );
};

export default ContactUs;
