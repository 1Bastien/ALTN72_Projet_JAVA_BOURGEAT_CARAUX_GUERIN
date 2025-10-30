/**
 * Initialise la recherche d'étudiants dans la modal
 */
document.addEventListener("DOMContentLoaded", function () {
  const searchForm = document.getElementById("searchForm");
  const searchResults = document.getElementById("searchResults");
  const resultsList = document.getElementById("resultsList");
  const resultsCount = document.getElementById("resultsCount");
  const searchLoading = document.getElementById("searchLoading");
  const searchModal = document.getElementById("searchModal");

  if (!searchForm) {
    return;
  }

  searchModal.addEventListener("show.bs.modal", function () {
    searchForm.reset();
    searchResults.classList.add("d-none");
    resultsList.innerHTML = "";
  });

  searchForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = {
      name: document.getElementById("searchName").value.trim() || null,
      company: document.getElementById("searchCompany").value.trim() || null,
      missionKeyword:
        document.getElementById("searchMissionKeyword").value.trim() || null,
      academicYear:
        document.getElementById("searchAcademicYear").value.trim() || null,
    };

    searchLoading.classList.remove("d-none");
    searchResults.classList.add("d-none");

    fetch("/search", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Erreur lors de la recherche");
        }
        return response.json();
      })
      .then((students) => {
        displaySearchResults(students);
      })
      .catch((error) => {
        console.error("Erreur lors de la recherche:", error);
        alert(
          "Une erreur est survenue lors de la recherche. Veuillez réessayer."
        );
      })
      .finally(() => {
        searchLoading.classList.add("d-none");
      });
  });

  searchForm.addEventListener("reset", function () {
    searchResults.classList.add("d-none");
    resultsList.innerHTML = "";
  });

  /**
   * Affiche les résultats de recherche
   */
  function displaySearchResults(students) {
    resultsList.innerHTML = "";

    if (students.length === 0) {
      resultsCount.textContent = "Aucun résultat trouvé";
      searchResults.classList.remove("d-none");
      return;
    }

    const studentText =
      students.length === 1 ? "apprenti trouvé" : "apprentis trouvés";
    resultsCount.textContent = `${students.length} ${studentText}`;

    students.forEach((student) => {
      const item = createStudentItem(student);
      resultsList.appendChild(item);
    });

    searchResults.classList.remove("d-none");
  }

  /**
   * Crée un élément de liste pour un étudiant
   */
  function createStudentItem(student) {
    const item = document.createElement("a");
    item.href = "#";
    item.className =
      "list-group-item list-group-item-action d-flex justify-content-between align-items-center";

    const program =
      student.schoolYears && student.schoolYears.length > 0
        ? student.schoolYears[0].program
        : "N/A";

    const academicYear =
      student.schoolYears && student.schoolYears.length > 0
        ? student.schoolYears[0].academicYear
        : null;

    item.innerHTML = `
      <div>
        <h6 class="mb-1">${student.lastName || ""} ${
      student.firstName || ""
    }</h6>
        <small class="text-muted">
          ${student.email || ""}
          ${program ? `• ${program}` : ""}
          ${academicYear ? `• ${academicYear}` : ""}
        </small>
      </div>
      <i class="fas fa-chevron-right text-muted"></i>
    `;

    item.addEventListener("click", function (e) {
      e.preventDefault();
      if (student.id && academicYear) {
        const formattedYear = academicYear.replace("/", "-");
        window.location.href = `/student/${student.id}/${formattedYear}`;
      }
    });

    return item;
  }
});
