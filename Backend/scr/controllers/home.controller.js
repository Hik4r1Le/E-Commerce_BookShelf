import HomeService from "../services/home.service.js"

class HomeController {
    async getHomeData (req, res, next) {
        try {
            const homeDatas = await HomeService.getProducts(req);
            res.status(200).json({ success: true, data: homeDatas });
        } catch (err) {
            next(err);
        }
    }
}

export default new HomeController();
