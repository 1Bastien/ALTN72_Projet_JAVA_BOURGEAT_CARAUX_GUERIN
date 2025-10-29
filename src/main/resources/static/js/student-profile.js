/**
 * Initialise le filtrage des maîtres d'apprentissage par entreprise
 */
function initializeMentorFilter() {
  const companySelect = document.getElementById("company");
  const mentorSelect = document.getElementById("mentor");

  if (companySelect && mentorSelect) {
    const allMentorOptions = Array.from(mentorSelect.options);

    companySelect.addEventListener("change", function () {
      const selectedCompanyId = this.value;

      mentorSelect.innerHTML =
        '<option value="">Sélectionner un maître d\'apprentissage</option>';

      allMentorOptions.forEach((option) => {
        if (option.value === "") {
          return;
        }

        const mentorCompanyId = option.getAttribute("data-company-id");
        if (!selectedCompanyId || mentorCompanyId === selectedCompanyId) {
          mentorSelect.appendChild(option.cloneNode(true));
        }
      });

      if (mentorSelect.options.length === 2) {
        mentorSelect.selectedIndex = 1;
      }
    });

    if (companySelect.value) {
      companySelect.dispatchEvent(new Event("change"));
    }
  }
}

/**
 * Initialise la synchronisation du sélecteur d'entreprise dans le modal de maître d'apprentissage
 */
function initializeMentorModalCompanySync() {
  const companySelect = document.getElementById("company");
  const mentorCompanySelect = document.getElementById("mentorCompany");
  const addMentorModal = document.getElementById("addMentorModal");

  if (companySelect && mentorCompanySelect && addMentorModal) {
    addMentorModal.addEventListener("show.bs.modal", function () {
      if (companySelect.value) {
        mentorCompanySelect.value = companySelect.value;
      }
    });
  }
}

/**
 * Combine la date et l'heure en un seul champ datetime pour l'envoi au serveur
 */
function combineDateTime(dateFieldId, timeFieldId, hiddenFieldId) {
  const dateValue = document.getElementById(dateFieldId).value;
  const timeValue = document.getElementById(timeFieldId).value;

  if (dateValue && timeValue) {
    const dateTimeValue = `${dateValue}T${timeValue}:00`;
    document.getElementById(hiddenFieldId).value = dateTimeValue;
    return true;
  }

  return false;
}

/**
 * Cette fonction permet d'initialiser la modification d'une visite et la modal
 */
function initializeEditVisitButtons() {
  const editButtons = document.querySelectorAll(".edit-visit-btn");

  editButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const visitId = this.getAttribute("data-visit-id");
      const date = this.getAttribute("data-visit-date");
      const time = this.getAttribute("data-visit-time");
      const format = this.getAttribute("data-visit-format");
      const comment = this.getAttribute("data-visit-comment");

      const editVisitModal = new bootstrap.Modal(
        document.getElementById("editVisitModal")
      );
      const editVisitForm = document.getElementById("editVisitForm");

      editVisitForm.action = `/student/${studentId}/visit/${visitId}/update`;

      document.getElementById("editVisitDate").value = date;
      document.getElementById("editVisitTime").value = time;
      document.getElementById("editVisitFormat").value = format;
      document.getElementById("editVisitComment").value = comment;

      editVisitModal.show();
    });
  });
}

/**
 * Initialise tous les événements au chargement de la page
 */
function initializeStudentProfile() {
  initializeMentorFilter();
  initializeMentorModalCompanySync();
  initializeEditVisitButtons();
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initializeStudentProfile);
} else {
  initializeStudentProfile();
}
