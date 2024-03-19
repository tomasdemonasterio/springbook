const searchInput = document.getElementById('searchInput');
const searchResults = document.getElementById('searchResults');

var usersJson= null;

fetch("http://localhost:8080/api/users")
    .then(response => response.json())
    .then(data => {usersJson = data})
    .catch(error => console.log(error));

function search(query) {


    searchResults.innerHTML = '';
    const filteredUsernames = usersJson.filter(item => item.username.toLowerCase().includes(query.toLowerCase()));

    filteredUsernames.forEach(result => {
        const resultItem = document.createElement('a');
        resultItem.classList.add('result-item');
        resultItem.href = window.location.href + "users/" + result.id;
        resultItem.textContent = result.username;
        resultItem.style = "display: block;";
        resultItem.addEventListener('click', () => {
            console.log('Clicked:', result);
        });
        searchResults.appendChild(resultItem);
    });

    if (filteredUsernames.length > 0) {
        searchResults.style.display = 'block';
    } else {
        searchResults.style.display = 'none';
    }
}

searchInput.addEventListener('input', () => {
    const query = searchInput.value.trim();
    if (query !== '') {
        search(query);
    } else {
        searchResults.innerHTML = '';
        searchResults.style.display = 'none';
    }
});


window.addEventListener('click', (event) => {
    if (!searchResults.contains(event.target) && event.target !== searchInput) {
        searchResults.style.display = 'none';
    }
});
