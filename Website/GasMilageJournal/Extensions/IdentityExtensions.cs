using Microsoft.AspNet.Authentication.Cookies;
using Microsoft.AspNet.Builder;
using Microsoft.AspNet.Http;
using Microsoft.AspNet.Identity;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.OptionsModel;
using System;

namespace GasMilageJournal.Extensions
{
    public static class IdentityExtensions
    {
        public static IApplicationBuilder UseCustomIdentity(this IApplicationBuilder app)
        {
            if (app == null) {
                throw new ArgumentNullException(nameof(app));
            }

            var options = app.ApplicationServices.GetRequiredService<IOptions<IdentityOptions>>().Value;

            app.UseCookieAuthentication(options.Cookies.ExternalCookie);
            app.UseCookieAuthentication(options.Cookies.TwoFactorRememberMeCookie);
            app.UseCookieAuthentication(options.Cookies.TwoFactorUserIdCookie);

            app.UseCookieAuthentication(new CookieAuthenticationOptions
            {
                AccessDeniedPath = new PathString("/Unauthorized"),
                CookieName = ".GAS",
                ExpireTimeSpan = new TimeSpan(0, 30, 0),
                SlidingExpiration = true,
                AuthenticationScheme = options.Cookies.ApplicationCookieAuthenticationScheme,
                AutomaticAuthenticate = true,
                AutomaticChallenge = true,
                Events = new CookieAuthenticationEvents
                {
                    OnValidatePrincipal = SecurityStampValidator.ValidatePrincipalAsync
                }
            });

            return app;
        }
    }
}
