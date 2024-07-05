document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const response = await fetch('/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    });

    const result = await response.json();
    const messageDiv = document.getElementById('message');
    if (result.flag) {
        messageDiv.textContent = result.msg;
        window.location.href = result.redirectUrl;
    } else {
        messageDiv.textContent = result.msg;
    }
});
