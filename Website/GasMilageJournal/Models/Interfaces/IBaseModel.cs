using System;

namespace GasMilageJournal.Models.Interfaces
{
    public interface IBaseModel<TKey>
    {
        TKey Id { get; set; }
    }

    public interface IBaseModel : IBaseModel<Guid>
    {
        DateTime Created { get; set; }
        string UserId { get; set; }
    }
}
