const express = require('express');
const bodyParser = require('body-parser');
const http = require('http');
const fs = require('fs');
const path = require('path');

const app = express();
app.use(bodyParser.urlencoded({ extended: true }));

// Serve the HTML form
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'reg.html'));
});

// Handle form submission
app.post('/register', (req, res) => {
    const { username, email, phone, clubname, teamname } = req.body;

    // Create a message to be sent via email
    const message = `
    Hello ${username},

    Thank you for registering. Here are the details you provided:
    Username: ${username}
    Email: ${email}
    Phone: ${phone}
    Club Name: ${clubname}
    Team Name: ${teamname}
    `;

    // Simulate email sending by writing it to a text file (this step replaces real email sending)
    fs.writeFile('user-details.txt', message, (err) => {
        if (err) throw err;
        console.log('User details have been saved.');
    });

    // Confirm registration and notify the user
    res.send(`Registration successful! A confirmation email will be sent to ${email}.`);
});

// Start the server
app.listen(8080, () => {
    console.log('Server running on http://localhost:8080');
});
