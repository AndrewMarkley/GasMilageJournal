using GasMilageJournal.Models;
using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Http;
using System.Linq;
using System.Security.Claims;

namespace GasMilageJournal.Services
{
    public class ApplicationService : IApplicationService
    {
        private readonly DataContext _dataContext;
        private readonly IHttpContextAccessor _httpContext;

        private User _currentUser;

        public string UserId { get; set; }

        public ApplicationService(DataContext dataContext, IHttpContextAccessor httpContext)
        {
            _dataContext = dataContext;
            _httpContext = httpContext;

            if (_httpContext != null) {
                UserId = _httpContext.HttpContext.User.GetUserId();
            }
        }

        public User GetUser()
        {
            if (_currentUser != null) {
                return _currentUser;
            }

            _currentUser = _dataContext.Users.Where(t => t.Id == UserId.ToString()).Single();

            return _currentUser;
        }

        public User User => GetUser();
    }
}
