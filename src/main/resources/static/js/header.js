/**
 * Initialise le bouton de création de la prochaine année académique
 */
function initializeCreateNextYearButton() {
  const createNextYearBtn = document.getElementById("createNextYearBtn");

  if (!createNextYearBtn) {
    return;
  }

  createNextYearBtn.addEventListener("click", function (e) {
    e.preventDefault();
    e.stopPropagation();

    const currentYear = document.getElementById("selectedYear").textContent;

    if (!currentYear) {
      alert("Impossible de déterminer l'année académique actuelle");
      return;
    }

    const years = currentYear.split("/");
    const startYear = parseInt(years[0]);
    const nextYear = startYear + 1 + "/" + (startYear + 2);

    const confirmed = confirm(
      `Êtes-vous sûr de vouloir créer l'année académique ${nextYear} pour tous les étudiants de l'année ${currentYear} ?\n\n` +
        `Cette action va :\n` +
        `- Créer une nouvelle année scolaire avec le programme suivant pour chaque étudiant\n` +
        `- Archiver les étudiants qui sont actuellement en M2_APP\n` +
        `- Ignorer les étudiants qui ont déjà une année pour ${nextYear}`
    );

    if (confirmed) {
      const form = document.createElement("form");
      form.method = "POST";
      form.action = "/progress-year";

      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "academicYear";
      input.value = currentYear;

      form.appendChild(input);
      document.body.appendChild(form);
      form.submit();
    }
  });
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initializeCreateNextYearButton);
} else {
  initializeCreateNextYearButton();
}
