"use strict";
console.log("js file")
const response = await fetch("/werknemers/aantal");
if (response.ok) {
    console.log("ok")
    const aantalWerknemers = await response.text();
    document.getElementById("aantal").innerText = response.text().catch(reason => console.log(reason));
} else {
    alert("Technische storing");
}