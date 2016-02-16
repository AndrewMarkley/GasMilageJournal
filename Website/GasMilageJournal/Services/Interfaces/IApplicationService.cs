using GasMilageJournal.Models;
using System;

namespace GasMilageJournal.Services.Interfaces
{
    public interface IApplicationService
    {
        User User { get; }
        string UserId { get; set; }
    }
}
