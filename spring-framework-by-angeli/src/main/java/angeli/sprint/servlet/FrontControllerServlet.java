package angeli.sprint.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import angeli.sprint.file.Fichier;
import angeli.sprint.model.ModelAndView;
import angeli.sprint.session.SessionManager;
import angeli.sprint.url.URLMethod;
import angeli.sprint.utils.URLParser;
import angeli.sprint.utils.reflect.method.Invoker;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import angeli.sprint.utils.URLChecker;

/**
 * Servlet du spring-framework-by-Angeli.
 * Point d'entree unique : recoit toutes les requetes, trouve la methode de
 * controller associee a l'URL et ecrit son resultat (vue, JSON ou texte).
 * Les routes sont preparees au demarrage par le
 * {@link angeli.sprint.servlet.listener.ContextListener}.
 */
public class FrontControllerServlet extends HttpServlet {

    /**
     * La liste des classes avec l'annotation @Controller
     */
    List<String> controllerList;
    /** Les methodes annotees {@link angeli.sprint.annotation.URL}. */
    List<Method> methodList;
    /** Routes GET : URL vers methode. */
    Map<String, URLMethod> urlMethodMapGET;
    /** Routes POST : URL vers methode. */
    Map<String, URLMethod> urlMethodMapPOST;
    /** Fournit les sessions aux controllers. */
    SessionManager sessionManager;

    /** L'extension du fichier */
    String prefix;

    /** Le debut du chemin du fichier */
    String suffix;

    /**
     * Traite une requete GET.
     *
     * @param req la requete http
     * @param rep la reponse http
     * @throws IOException      si la reponse ne peut pas etre ecrite
     * @throws ServletException en cas d'erreur du servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse rep) throws IOException, ServletException {
        ProcessRequest(req, rep, "GET");
    }

    /**
     * Traite une requete POST.
     *
     * @param req la requete http
     * @param rep la reponse http
     * @throws IOException      si la reponse ne peut pas etre ecrite
     * @throws ServletException en cas d'erreur du servlet
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse rep) throws IOException, ServletException {
        ProcessRequest(req, rep, "POST");
    }

    /**
     * Traite une requete : recupere les parametres et fichiers, cherche la
     * route puis, selon le retour de la methode, affiche la vue, ecrit la
     * reponse de l'API ou ecrit le resultat en JSON. Ecrit une 404 si l'URL
     * n'existe pas. Une requete deja forwardee est servie directement par le
     * servlet JSP.
     *
     * @param req    la requete http
     * @param rep    la reponse http
     * @param method la methode HTTP ("GET" ou "POST")
     * @throws IOException      si la reponse ne peut pas etre ecrite
     * @throws ServletException si les fichiers multipart ne peuvent pas etre lus
     */
    private void ProcessRequest(HttpServletRequest req, HttpServletResponse rep, String method)
            throws IOException, ServletException {
        System.out.println("===============================================================");

        sessionManager.setRequest(req);

        String url = URLParser.getUrlFromRequest(req)[1];

        // Affiche la methode utilisee
        System.out.println("Method: " + method);

        // Affiche l'url sur la page web
        System.out.println("Application url : " + req.getContextPath() + url);

        Map<String, URLMethod> urlMethodMap = null;

        switch (method) {
            case "GET":
                urlMethodMap = urlMethodMapGET;
                break;

            case "POST":
                urlMethodMap = urlMethodMapPOST;
                break;
        }

        Map<String, String[]> args = null;

        // Manmijery izy raha manome zavatra .next()
        if (req.getParameterNames().hasMoreElements()) {
            args = req.getParameterMap();
        }
        Map<String, Fichier> fichiers = estMultipart(req) ? DataGeter.getFichiers(req) : null;
        System.out.println("est  multipart: " + estMultipart(req));
        args = DataGeter.addFileNamesToMap(fichiers, args);

        boolean[] checkUrl = URLChecker.checkUrl(url, urlMethodMap);
        boolean UrlExists = checkUrl[0];
        boolean UrlHasView = checkUrl[1];
        boolean UrlisAnAPI = checkUrl[2];

        if (req.getAttribute("jakarta.servlet.forward.request_uri") != null) {
            String targetPath = url;

            // Afficher le path du jsp dans catalina.out
            System.out.println("Forward detecte, on sert: " + targetPath);

            //Cet attribut est celui de la resource jsp a servir
            req.setAttribute("org.apache.catalina.jsp_file", targetPath);
            try {

            /**
             * Le forward c'est un doGet ou un doPost(le navigateur voit ca comme un redirect COMMME !!!!) avec le dispatcher qui dit quelle est la resource specifiee : jsp par exemple,
             * Include traite la resource ici donc j'evite c chiant si la page se duplique
             */
                req.getServletContext().getNamedDispatcher("jsp").forward(req, rep);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return;
        }
        if (UrlExists && !UrlHasView && !UrlisAnAPI) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            // Dans ce cas si l'url a chemin et doit print du texte
            try {
                System.out.println("Ni vue ni API Mais Url Existe");
                PageWriter.print(req, rep, Invoker.invokeMethod(calledMethod, args, sessionManager, fichiers), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (UrlExists) {
            Method calledMethod = urlMethodMap.get(url).getMethod();
            // Si l'url est un objet different de modelAndView
            // Envoie en json
            if (UrlisAnAPI) {
                try {
                    PageWriter.print(req, rep, Invoker.invokeMethod(calledMethod, args, sessionManager, fichiers),
                            URLChecker.getContentTypeForUrl(url, urlMethodMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (UrlHasView) {
                try {
                    ModelAndView modelAndView = (ModelAndView) Invoker.invokeMethod(calledMethod, args, sessionManager,
                            fichiers);

                    // Print le ModelAndView en String dans catalina.out
                    System.out.println("ModelAndView: " + modelAndView);

                    view(modelAndView.getView(), suffix, prefix, req, rep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            PageWriter.urlNotFound(rep);
        }
    }

    /**
     * Forward vers la vue dont le chemin est {@code suffix + viewName + prefix}.
     * Attention : ici "suffix" est le debut du chemin et "prefix" la fin.
     *
     * @param viewName le nom de la vue, sans dossier ni extension
     * @param suffix   debut du chemin, valeur du parametre "suffix" du web.xml (ex : /WEB-INF/views/)
     * @param prefix   fin du chemin, valeur du parametre "prefix" du web.xml (ex : .jsp)
     * @param req      la requete http
     * @param rep      la reponse http
     * @throws IOException si le forward echoue
     */
    public void view(String viewName, String suffix, String prefix, HttpServletRequest req, HttpServletResponse rep)
            throws IOException {
        String viewPath = suffix + viewName + prefix;

        // Print le chemin de la vue dans catalina.out
        System.out.println("View Path: " + viewPath);

        try {
            req.getRequestDispatcher(viewPath).forward(req, rep);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifie si la requete est multipart (pour les fichiers)
     *
     * @param req la requete http
     * @return true si le Content-Type commence par "multipart/"
     */
    boolean estMultipart(HttpServletRequest req) {
        String type = req.getContentType();
        return type != null && type.toLowerCase().startsWith("multipart/");
    }

    /**
     * Initialisation du servlet, recupere la liste des controllers, les routes
     * GET/POST et le SessionManager depuis le context du servlet
     *
     * @throws ServletException si l'initialisation echoue
     */
    @SuppressWarnings("unchecked")
    @Override
    public void init() throws ServletException {
        super.init();
        controllerList = (List<String>) getServletContext().getAttribute("controllerList");
        methodList = (List<Method>) getServletContext().getAttribute("urlMethods");
        urlMethodMapGET = (Map<String, URLMethod>) getServletContext().getAttribute("urlMethodMapGET");
        urlMethodMapPOST = (Map<String, URLMethod>) getServletContext().getAttribute("urlMethodMapPOST");
        sessionManager = (SessionManager) getServletContext().getAttribute("sessionManager");
        prefix = (String) getServletContext().getInitParameter("angeli.sprint.prefix");
        suffix = (String) getServletContext().getInitParameter("angeli.sprint.suffix");

    }
}
