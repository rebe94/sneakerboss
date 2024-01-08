<h1 align="center">
    <a href="https://sneakerboss-rebe94.koyeb.app/"><img src="https://i.ibb.co/gwGX9Cq/image.jpg" alt="logo" border="0" style="width:30%;"> </a>
</h1>
<h3 align="center">Sneakerboss - monitor sneakers on the most popular streetwear market StockX </h3>
<p align="center">
    Open App here: <a href="https://sneakerboss-rebe94.koyeb.app/">https://sneakerboss-rebe94.koyeb.app/</a></p>
<p align="center">
Check it out, main app's source code on Github: <a href="https://github.com/rebe94/sneakerboss">https://github.com/rebe94/sneakerboss</a></p>



[Sneakerboss](https://sneakerboss-rebe94.koyeb.app/) project was created in order to help my friends in monitoring products from [StockX](https://stockx.com/) market.

​	The web application allows to search streetwear products with its current market price and also monitor them. The application are built on `modular architecture` with entrypoint for every module which is `Facade design pattern`. The application is covered by BDD tests to provide the system working assurance.
Under the hood sneakerboss app calls `StockX API` and official `polish central bank NBP API` to get exchange currency rate.   
The application is available 24/7 thanks to `Koyeb web hosting`, it was deployed via `Docker file` configuration.
The project is being constantly developed.

# Tech stack

- Backend: `Kotlin`, `SpringBoot` - (`Web`, `Data`, `Test`)
  - NoSQL Database - `MongoDB`
  - Testing libraries - `JUnit`, `Mockito`, `AssertJ`
- Simple Frontend: `Thymeleaf`
- Deploying: `Docker`, `MongoDB Atlas`, `Koyeb web hosting`