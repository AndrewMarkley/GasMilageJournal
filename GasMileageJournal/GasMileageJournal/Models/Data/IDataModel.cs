using System;

namespace GasMileageJournal.Models.Data
{
    interface IDataModel
    {
        String Id { get; set; }
        DateTime CreatedOn { get; set; }
        DateTime UpdatedOn { get; set; }
        String UserId { get; set; }
    }
}