/**
 * Permet de sélectionner une année académique et de rediriger vers la page correspondante
 */

function initializeYearSelector() {
  const dropdownItems = document.querySelectorAll(".dropdown-item");

  dropdownItems.forEach((item) => {
    item.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();

      const selectedYear = this.getAttribute("data-year");

      if (selectedYear) {
        const selectedYearSpan = document.getElementById("selectedYear");
        if (selectedYearSpan) {
          selectedYearSpan.textContent = selectedYear;
        }

        const formattedYear = selectedYear.replace("/", "-");
        const newUrl = `/${formattedYear}`;
        window.location.href = newUrl;
      }
    });
  });
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initializeYearSelector);
} else {
  initializeYearSelector();
}
