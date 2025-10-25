/**
 * Pré-rempli l'année courante dans le formulaire de création d'étudiant pour l'année scolaire en cours
 */

document.addEventListener("DOMContentLoaded", function () {
  const yearSelect = document.querySelector(
    'select[name="schoolYear.academicYear"]'
  );
  if (yearSelect) {
    const currentYear = new Date().getFullYear();
    const currentAcademicYear = `${currentYear}/${currentYear + 1}`;

    for (let option of yearSelect.options) {
      if (option.value === currentAcademicYear) {
        option.selected = true;
        break;
      }
    }
  }
});
