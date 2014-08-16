using System.Web.Http;
using System.Web.Mvc;
using System.Web.Routing;
using GasMileageJournal.Areas.Api.Security;

namespace GasMileageJournal
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            routes.MapHttpRoute("Api", "Api/{controller}/{action}/{*value}", new { value = UrlParameter.Optional },
                null, new ApiSecurityHandler(GlobalConfiguration.Configuration)
                );

            routes.MapRoute("Default", "{controller}/{action}/{*value}",
                new { controller = "Login", action = "Index", value = UrlParameter.Optional },
                new[] { "GasMileageJournal.Controllers" }
                );
        }
    }
}
