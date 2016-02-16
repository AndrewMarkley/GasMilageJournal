using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Mvc.ModelBinding;
using System.Linq;

namespace GasMilageJournal.Extensions
{
    public static class ModelStateExtensions
    {
        public static void AddAllMessages<T>(this ModelStateDictionary modelState, IServiceResult<T> result)
        {
            modelState.AddModelInfoMessages(result);
            modelState.AddModelWarnings(result);
            modelState.AddModelErrors(result);
        }

        public static void AddModelInfoMessages<T>(this ModelStateDictionary modelState, IServiceResult<T> result)
        {
            foreach (var error in result.Messages.Where(t => t.Type == ServiceResultMessageType.Info)) {
                modelState.AddModelError("Information", error.Message);
            }
        }

        public static void AddModelWarnings<T>(this ModelStateDictionary modelState, IServiceResult<T> result)
        {
            foreach (var error in result.Messages.Where(t => t.Type == ServiceResultMessageType.Warning)) {
                modelState.AddModelError("Warning(s)", error.Message);
            }
        }

        public static void AddModelErrors<T>(this ModelStateDictionary modelState, IServiceResult<T> result)
        {
            foreach (var error in result.Messages.Where(t => t.Type == ServiceResultMessageType.Error)) {
                modelState.AddModelError("Error(s)", error.Message);
            }
        }
    }
}
