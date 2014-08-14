using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(GasMileageJournal.Startup))]
namespace GasMileageJournal
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
