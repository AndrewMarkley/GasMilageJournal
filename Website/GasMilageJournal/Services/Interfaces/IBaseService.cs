using System;
using System.Threading.Tasks;

namespace GasMilageJournal.Services.Interfaces
{
    public interface IBaseService<T>
    {
        Task<ServiceResult> Edit(string id);
        Task<ServiceResult> DeleteAsync(Guid id);
        Task<ServiceResult> DeleteAsync(T car);
        Task<ServiceResult> GetByIdAsync(Guid id);
        Task<ServiceResult> GetAllAsync();
        Task<ServiceResult> SaveAsync(T car);
    }
}
