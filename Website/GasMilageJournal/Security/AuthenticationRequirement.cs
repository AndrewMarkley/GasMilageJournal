using GasMilageJournal.Models;
using GasMilageJournal.Services;
using Microsoft.AspNet.Authorization;
using System;
using System.Security.Claims;

namespace GasMilageJournal.Security
{
    public class AuthenticationRequirement : AuthorizationHandler<AuthenticationRequirement>, IAuthorizationRequirement
    {
        protected override void Handle(AuthorizationContext context, AuthenticationRequirement requirement)
        {
            try {
                if (context?.User?.Identity == null) {
                    context.Fail();

                    return;
                }

                var user = (new ApplicationService(DataContext.GetInstance(), null)
                {
                    UserId = context.User.GetUserId()
                }).User;

                if (user == null) {
                    context.Fail();

                    return;
                }

                context.Succeed(requirement);
            } catch (Exception ex) {
                throw ex;
            }
        }
    }
}
