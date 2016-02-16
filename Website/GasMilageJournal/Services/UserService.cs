using GasMilageJournal.Services.Interfaces;
using System;
using System.Linq;
using System.Threading.Tasks;
using GasMilageJournal.Models;
using Microsoft.Data.Entity;
using Microsoft.AspNet.Identity;

namespace GasMilageJournal.Services
{
    public class UserService : IUserService
    {
        private DataContext _dataContext;
        private IApplicationService _appService;
        private readonly UserManager<User> _userManager;

        public UserService(DataContext dataContext, IApplicationService appService, UserManager<User> userManager)
        {
            _dataContext = dataContext;
            _appService = appService;
            _userManager = userManager;
        }

        public async Task<ServiceResult> DeleteAsync(Guid id)
        {
            try {
                var user = await _dataContext.Users.Where(t => t.Id == id.ToString())
                                                       .SingleOrDefaultAsync();

                await DeleteAsync(user);

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> DeleteAsync(User user)
        {
            try {
                _dataContext.Users.Remove(user);
                await _dataContext.SaveChangesAsync();

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> Edit(string id)
        {
            try {
                User user = await _dataContext.Users.Where(t => t.Id == _appService.UserId)
                                                    .Where(t => t.Id == id)
                                                    .SingleAsync();

                return new ServiceResult(user);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetAllAsync()
        {
            try {
                var users = await _dataContext.Users.ToListAsync();

                return new ServiceResult(users);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetByIdAsync(Guid id)
        {
            try {
                var user = await _dataContext.Users.Where(t => t.Id == id.ToString())
                                                       .SingleOrDefaultAsync();

                return new ServiceResult(user);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> SaveAsync(User user)
        {
            try {
                _dataContext.Update(user);
                _dataContext.SaveChanges();

                return new ServiceResult(user);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }
    }
}
